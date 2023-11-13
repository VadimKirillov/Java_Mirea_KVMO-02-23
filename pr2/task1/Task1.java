import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Task1 {
    public static File createFile(final String filename, final String pathname, final long sizeInBytes) throws IOException {
        File file = new File(pathname + File.separator + filename);
        file.createNewFile();

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.setLength(sizeInBytes);
        raf.close();

        return file;
    }

    public static void main(String[] args) throws IOException {
        String currentDir = System.getProperty("user.dir");
        new File(currentDir+"/tmp").mkdir(); // Создание файла
        Path tmpDir = Paths.get(currentDir, "tmp");
        System.out.println("Working directory is - "+tmpDir); //Вывод пути к рабочей директории

        // Создание файла с текстом
        File myFile = createFile("file.txt", tmpDir.toString(),0);
        String str = "Начало\nПродолжение\nЕщё строка\nКонец\n...";
        byte[] bs = str.getBytes();
        Files.write(myFile.toPath(), bs); // Запись в файл

        // Вывод из файла
        System.out.println("File "+myFile.getName()+" contents:\n" + new String(Files.readAllBytes(myFile.toPath())));
    }
}