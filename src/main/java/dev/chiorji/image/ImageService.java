package dev.chiorji.image;

import com.cloudinary.*;
import com.cloudinary.utils.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

@Service
public class ImageService {
	private static final Logger log = LoggerFactory.getLogger(ImageService.class);
	private final ImageRepository imageRepository;
	private final Cloudinary cloudinary;

	public ImageService(ImageRepository imageRepository, Cloudinary cloudinary) {
		this.imageRepository = imageRepository;
		this.cloudinary = cloudinary;
	}

	public Image uploadImage(ImageUploadDTO imageUploadDTO) throws IOException {
		Map<String, Object> result = uploadToCloudinary(imageUploadDTO.url());
		ImageDTO imageDTO = new ImageDTO(imageUploadDTO.name(), (String) result.get("secure_url"), (String) result.get("asset_id"));
		return imageRepository.saveImage(imageDTO);
	}

	private Map<String, Object> uploadToCloudinary(MultipartFile multipartFile) throws IOException {
		File file = convert(multipartFile);
		log.info("Converted file to stream, uploading to cloudinary...");
		Map<String, Object> result = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "blog_covers"));
		if (!Files.deleteIfExists(file.toPath())) {
			log.error("Failed to delete temp file");
			throw new IOException("Failed to delete temp file " + file.getAbsolutePath());
		}
		log.info("File successfully uploaded to cloudinary");
		log.info("{}", result);
		return result;
	}

	private File convert(MultipartFile multipartFile) throws IOException {
		log.info("Converting file to stream");
		File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(multipartFile.getBytes());
		fileOutputStream.close();
		return file;
	}

	public void deleteImageById(Integer id) throws IOException {
		Image image = findImageById(id);
		cloudinary.uploader().destroy(image.asset_id(), ObjectUtils.emptyMap());
		imageRepository.deleteImageById(id);
	}

	public Image findImageById(Integer id) {
		return imageRepository.findImageById(id);
	}
}
