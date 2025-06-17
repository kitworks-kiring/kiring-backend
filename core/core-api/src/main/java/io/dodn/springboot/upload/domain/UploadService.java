package io.dodn.springboot.upload.domain;

import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.entity.Category;
import io.dodn.springboot.storage.db.matzip.entity.Menu;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UploadService {
    private static final Logger log = LoggerFactory.getLogger(UploadService.class);
    private final MatzipRepository matzipRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public UploadService(final MatzipRepository matzipRepository) {
        this.matzipRepository = matzipRepository;
    }

    @Transactional
    public int savePlaceDataFromExcel(final MultipartFile file) {
        List<Place> placesToSave = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(1) == null || row.getCell(1).getCellType() == CellType.BLANK) {
                    continue;
                }

                try {
                    String name = getStringCellValue(row, 1);
                    String naverCategory = getStringCellValue(row, 2);
                    String categoryString = getStringCellValue(row, 3);
                    String address = getStringCellValue(row, 4);
                    double latitude = getNumericCellValue(row, 5);
                    double longitude = getNumericCellValue(row, 6);
                    String phoneNumber = getStringCellValue(row, 7);
                    String menuString = getStringCellValue(row, 8); // 메뉴 문자열 (예: "꼬치,맥주")
                    String imageUrl = getStringCellValue(row, 9);

                    // 위도, 경도가 0이 아닌 유효한 값인지 확인
                    if (latitude == 0.0 || longitude == 0.0) {
                        log.warn("엑셀 파일의 {}번째 행에 유효하지 않은 좌표 정보가 있어 건너뜁니다: {}", i + 1, name);
                        continue;
                    }

                    Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
                    Place place = new Place(
                            name,
                            address,
                            naverCategory,
                            imageUrl,
                            phoneNumber,
                            null,
                            0,
                            location,
                            new ArrayList<>()
                    );
                    place.setCategories(new HashSet<>()); // 카테고리 Set 초기화

                    if (StringUtils.hasText(categoryString)) {
                        List<String> categoryNames = Arrays.stream(categoryString.split(","))
                                .map(String::trim)
                                .filter(StringUtils::hasText)
                                .toList();

                        // DB에서 이미 존재하는 카테고리들을 한 번에 조회
                        List<Category> existingCategories = matzipRepository.categoryFindByNameIn(categoryNames);
                        Set<String> existingCategoryNames = existingCategories.stream()
                                .map(Category::getName)
                                .collect(Collectors.toSet());

                        // DB에 없는 새로운 카테고리들을 찾아 생성
                        for (String categoryName : categoryNames) {
                            if (!existingCategoryNames.contains(categoryName)) {
                                Category newCategory = new Category(categoryName);
                                existingCategories.add(matzipRepository.saveCategory(newCategory));
                            }
                        }

                        existingCategories.forEach(place::addCategory);
                    }

                    if (StringUtils.hasText(menuString)) {
                        Arrays.stream(menuString.split(","))
                                .map(String::trim)
                                .filter(StringUtils::hasText)
                                .forEach(menuName -> {
                                    Menu menu = new Menu(menuName, 0, null, null, false, null);
                                    place.addMenu(menu);
                                });
                    }
                    log.info("### Excel Place : {}", place);
                    placesToSave.add(place);

                } catch (Exception e) {
                    log.error("엑셀 파일의 {}번째 행 처리 중 오류 발생: {}", i + 1, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("엑셀 파일을 읽는 중 오류가 발생했습니다.", e);
            throw new RuntimeException("엑셀 파일을 읽는 중 오류가 발생했습니다.", e);
        }

        matzipRepository.saveAll(placesToSave);
        return placesToSave.size();
    }

    // 셀의 문자열 값을 안전하게 가져오는 헬퍼 메서드
    private String getStringCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            return ""; // 빈 셀은 빈 문자열로 처리
        }
        // 셀 타입에 따라 다르게 처리
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            // 숫자가 문자열로 저장된 경우를 대비
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return "";
    }

    // 셀의 숫자 값을 안전하게 가져오는 헬퍼 메서드
    private double getNumericCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum);
        if (cell == null) {
            return 0.0;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        // 문자열로 된 숫자를 파싱
        try {
            return Double.parseDouble(cell.getStringCellValue());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
