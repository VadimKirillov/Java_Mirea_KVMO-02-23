import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Task3 {

    private static int sum(ByteBuffer bb) {
        int sum = 0;
        while (bb.hasRemaining()) {
            if ((sum & 1) != 0)
                sum = (sum >> 1) + 0x8000;
            else
                sum >>= 1;
            sum += bb.get() & 0xff;
            sum &= 0xffff;
        }
        return sum;
    }

    // Вычисление 16-ти битной контрольной суммы
    public static void sum(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();

        // Получаем размер файла, а затем отображаем его в память
        int sz = (int) fc.size();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);


        int sum = sum(bb);
        int kb = (sz + 1023) / 1024;
        String s = Integer.toString(sum);
        System.out.println(file.getName()+": FileSize "+kb+" KB, CheckSum="+s);

        // Закрытие канала и потока
        fc.close();
        fis.close();
    }

    public static void main(String[] args) throws IOException {
        String currentDir = System.getProperty("user.dir");
        new File(currentDir+"/tmp").mkdir();
        Path tmpDir = Paths.get(currentDir, "tmp");
        System.out.println("Working directory is - "+tmpDir); //Вывод пути к рабочей директории

        Scanner in = new Scanner(System.in);
        System.out.print("Ввод названия файла(с расширением): ");
        File myFile = new File(tmpDir + File.separator + in.nextLine());

        // Пробуем рассчитать контрольную сумму
        try {
            sum(myFile);
        } catch (IOException e) {
            System.err.println(myFile + ": " + e);
        }
    }
}