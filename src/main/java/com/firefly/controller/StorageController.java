package com.firefly.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.firefly.config.BlobHelper;
import com.firefly.config.StorageConfig;
import com.firefly.pojo.BlobUploadResult;
import com.firefly.pojo.Result;
import com.firefly.utils.BlobUtil;
import com.firefly.utils.ResultUtil;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

@RestController
@RequestMapping("/file")
public class StorageController {

	@Autowired
	private StorageConfig storageConfig;

	// 设置缩略图的宽高
	private static int thumbnailWidth = 150;
	private static int thumbnailHeight = 100;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Result uploadFile(@RequestParam(value = "id", required = false, defaultValue = "test") String id,
			@RequestParam(value = "type", required = false, defaultValue = "1") String type,
			@RequestPart("file") MultipartFile[] multipartFile) {

		List<BlobUploadResult> blobUploadEntities = new ArrayList<BlobUploadResult>();
		BlobUploadResult blobUploadEntity = new BlobUploadResult();
		try {
			if (multipartFile != null) {

				// 获取或创建container
				CloudBlobContainer blobContainer = BlobHelper.getBlobContainer(id.toLowerCase(), storageConfig);
				// CloudBlobContainer blobContainer =
				// BlobHelper.getBlobContainer(id.toLowerCase(), null);
				for (int i = 0; i < multipartFile.length; i++) {
					MultipartFile tempMultipartFile = multipartFile[i];
					if (!tempMultipartFile.isEmpty()) {
						try {
							// 过滤非jpg,png,jpeg格式的文件
							if (!(tempMultipartFile.getContentType().toLowerCase().equals("image/jpg")
									|| tempMultipartFile.getContentType().toLowerCase().equals("image/jpeg")
									|| tempMultipartFile.getContentType().toLowerCase().equals("image/png"))) {
								Result result = ResultUtil.error("文件上传格式不对，目前上传的文件类型只支持jpg、jpeg、png");
								return result;
							}

							// 拼装blob的名称(前缀名称+文件的md5值+文件扩展名称)
							String checkSum = BlobUtil.getMD5(tempMultipartFile.getInputStream());
							String fileExtension = getFileExtension(tempMultipartFile.getOriginalFilename())
									.toLowerCase();
							String preName = getBlobPreName(type, false).toLowerCase();
							String blobName = preName + checkSum + fileExtension;

							// 设置文件类型，并且上传到azure blob
							CloudBlockBlob blob = blobContainer.getBlockBlobReference(blobName);
							blob.getProperties().setContentType(tempMultipartFile.getContentType());
							InputStream inPutStream = tempMultipartFile.getInputStream();
							long size = tempMultipartFile.getSize();
							blob.upload(inPutStream, size);

//							// 生成缩略图，并上传至AzureStorage
//							BufferedImage img = new BufferedImage(thumbnailWidth, thumbnailHeight,
//									BufferedImage.TYPE_INT_RGB);
//							img.createGraphics().drawImage(ImageIO.read(tempMultipartFile.getInputStream())
//									.getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH), 0, 0,
//									null);
//							ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
//							ImageIO.write(img, "jpg", thumbnailStream);
//							InputStream inputStream = new ByteArrayInputStream(thumbnailStream.toByteArray());
//
//							String thumbnailPreName = getBlobPreName(type, true).toLowerCase();
//							String thumbnailCheckSum = BlobUtil
//									.getMD5(new ByteArrayInputStream(thumbnailStream.toByteArray()));
//							String blobThumbnail = thumbnailPreName + thumbnailCheckSum + ".jpg";
//							CloudBlockBlob thumbnailBlob = blobContainer.getBlockBlobReference(blobThumbnail);
//							thumbnailBlob.getProperties().setContentType("image/jpeg");
//							thumbnailBlob.upload(inputStream, thumbnailStream.toByteArray().length);

							// 将上传后的图片URL返回
						
							blobUploadEntity.setFileName(tempMultipartFile.getOriginalFilename());
							blobUploadEntity.setFileUrl(blob.getUri().toString());
//							blobUploadEntity.setThumbnailUrl(thumbnailBlob.getUri().toString());

							blobUploadEntities.add(blobUploadEntity);
						} catch (Exception e) {
							Result result = ResultUtil.error("上传文件失败");
							return result;
						}
					}
				}
			}
		} catch (Exception e) {
			Result result = ResultUtil.error("上传文件失败");
			return result;
		}

		Result result = ResultUtil.success("上传文件成功", blobUploadEntity);
		return result;
	}

	private String getFileExtension(String fileName) {
		int position = fileName.indexOf('.');
		if (position > 0) {
			String temp = fileName.substring(position);
			return temp;
		}
		return "";
	}

	private String getBlobPreName(String fileType, Boolean thumbnail) {
		String afterName = "";
		if (thumbnail) {
			afterName = "thumbnail/";
		}

		switch (fileType) {
		case "1":
			return "logo/" + afterName;
		case "2":
			return "food/" + afterName;
		case "3":
			return "head/" + afterName;
		case "4":
			return "ads/" + afterName;
		default:
			return "";
		}
	}
}
