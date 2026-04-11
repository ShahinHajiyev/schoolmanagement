package shako.schoolmanagement.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shako.schoolmanagement.dto.EnrollmentDto;
import shako.schoolmanagement.dto.GpaDto;
import shako.schoolmanagement.dto.StudentResponseDto;
import shako.schoolmanagement.dto.TranscriptItemDto;

import java.util.List;

public interface StudentService {

    Page<StudentResponseDto> getAll(Pageable pageable);

    List<EnrollmentDto> getEnrollmentsByNeptunCode(String neptunCode);

    GpaDto getGpa(String neptunCode);

    List<TranscriptItemDto> getTranscript(String neptunCode);
}
