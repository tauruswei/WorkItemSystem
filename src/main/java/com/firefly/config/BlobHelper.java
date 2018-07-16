package com.firefly.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class BlobHelper {

	public static CloudBlobContainer getBlobContainer(String containerName, StorageConfig storageConfig) {
		try {
			String blobStorageConnectionString = String.format(
					"DefaultEndpointsProtocol=%s;" + "AccountName=%s;" + "AccountKey=%s;" + "EndpointSuffix=%s",
					storageConfig.getDefaultEndpointsProtocol(), storageConfig.getAccountName(),
					storageConfig.getAccountKey(), storageConfig.getEndpointsuffix());

			CloudStorageAccount account = CloudStorageAccount.parse(blobStorageConnectionString);

			// CloudBlobClient 类是 Windows Azure Blob Service
			// 客户端的逻辑表示，我们需要使用它来配置和执行对 Blob Storage 的操作。
			CloudBlobClient serviceClient = account.createCloudBlobClient();

			// CloudBlobContainer 表示一个 Blob Container 对象
			CloudBlobContainer container = serviceClient.getContainerReference(containerName);

			// Create a permissions object.
			BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

			// Include public access in the permissions object.
			containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

			// Set the permissions on the container.
			container.uploadPermissions(containerPermissions);

			// 如果不存在就创建名为 containerName 的 Blob Container。
			// System.out.println("Creating container: " + container.getName());
			// container.createIfNotExists(new BlobRequestOptions(), new
			// OperationContext());
			container.createIfNotExists();

			// File sourceFile = File.createTempFile("sampleFile", ".txt");
			// System.out.println("Creating a sample file at: " +
			// sourceFile.toString());
			// Writer output = new BufferedWriter(new FileWriter(sourceFile));
			// output.write("Hello Azure!");
			// output.close();
			//
			// CloudBlockBlob blob =
			// container.getBlockBlobReference(sourceFile.getName());
			//
			// //Creating blob and uploading file to it
			// System.out.println("Uploading the sample file ");
			// blob.uploadFromFile(sourceFile.getAbsolutePath());

			// Iterable<ListBlobItem> list= container.listBlobs();
			// for(ListBlobItem blobItem : container.listBlobs()){
			// System.out.println("URI of blob is: " + blobItem.getUri());
			// }

			return container;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
