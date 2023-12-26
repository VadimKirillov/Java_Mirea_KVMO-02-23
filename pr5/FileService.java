package pr5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import pr5.BinaryFile;
import pr5.BinaryFileRepository;

// Сервис для работы с бинарными файлами
@Service
public class FileService {
    @Autowired
    BinaryFileRepository repository;

    // Загрузка бинарного файла
    public void uploadFile(String name, byte[] data) {
        // Проверка наличия файла с указанным именем в репозитории
        if (!repository.existsByFileName(name)) {
            // Создание объекта BinaryFile и сохранение в репозиторий
            BinaryFile file = new BinaryFile(name, data);
            repository.save(file);
        }
    }

    // Метод для скачивания бинарного файла по имени
    public byte[] downloadFile(String name) {
        // Получение объекта BinaryFile из репозитория по имени
        BinaryFile binaryFile = repository.findByFileName(name);
        return binaryFile.getFileData();
    }

    // Метод для определения типа медиа по расширению файла
    public String getMediaType(String name) {
        String getFormat = name.split("\\.")[1];
        String type = null;

        // Определение типа медиа в зависимости от формата
        switch (getFormat) {
            case "pdf" :
                type = MediaType.APPLICATION_PDF_VALUE;
                break;
            case "txt" :
                type = MediaType.TEXT_PLAIN_VALUE;
                break;
            case "jpg", "jpeg" :
                type = MediaType.IMAGE_JPEG_VALUE;
                break;
            case "png" :
                type = MediaType.IMAGE_PNG_VALUE;
                break;
        }

        // Возвращение определенного типа медиа
        return type;
    }
}