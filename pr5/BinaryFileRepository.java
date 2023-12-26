package pr5;

import org.springframework.data.jpa.repository.JpaRepository;
import pr5.BinaryFile;

public interface BinaryFileRepository extends JpaRepository<BinaryFile, Integer> {
    BinaryFile findByFileName(String fileName);

    Boolean existsByFileName(String fileName);
}