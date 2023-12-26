package pr5;


import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pr5.FileService;

// Контроллер для обработки запросов
@RestController
public class FileController {

    // Автоматический инжект сервиса для работы с файлами
    @Autowired
    FileService service;

    // Метод для обработки POST-запроса на загрузку файла в базу данных
    @PostMapping("/upload")
    public ResponseEntity uploadToDB(@RequestParam("file") MultipartFile file) throws IOException {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        service.uploadFile(StringUtils.cleanPath(file.getOriginalFilename()), file.getBytes());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();
        // Возврат успешного ответа с URI для скачивания
        return ResponseEntity.ok(fileDownloadUri);
    }

    // Метод для обработки GET-запроса на скачивание файла из базы данных
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity downloadFromDB(@PathVariable String fileName) {
        // Получение MIME-типа файла
        String mediaType = service.getMediaType(fileName);
        // Возврат ответа с данными файла
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mediaType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(service.downloadFile(fileName));
    }
}