package com.firefly.pojo;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;

public class test {
	
	public static final String storageConnectionString =
			"DefaultEndpointsProtocol=https;" +
			"AccountName=fireflystore;" +
			"AccountKey=hEE+HkD4Avm5qqmMbAgQuEFxaMr/RHl0XYYVo0X82sSO/gBJoF5EhmPzxqMQfVk27NnDjqbpzLgWL+3LpbyWrQ==;"
			+"EndpointSuffix=core.chinacloudapi.cn";
//	
	public static void main(String[] args) throws InvalidKeyException, URISyntaxException, StorageException {
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		CloudBlobContainer container = blobClient.getContainerReference("test");
		
		
		// Create the container if it does not exist with public access.
		System.out.println("Creating container: " + container.getName());
		container.createIfNotExists();
		
		for (ListBlobItem blobItem : container.listBlobs()) {
		    System.out.println("URI of blob is: " + blobItem.getUri());
		}
		
	}
//	
//	// Parse the connection string and create a blob client to interact with Blob storage
//
//	storageAccount = CloudStorageAccount.parse(storageConnectionString);
//	blobClient = storageAccount.createCloudBlobClient();
//	container = blobClient.getContainerReference("quickstartcontainer");
//	
//	// Create the container if it does not exist with public access.
//	System.out.println("Creating container: " + container.getName());
//	container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
//
//	//2、将 blob 上传到容器
//	
//	//Creating a sample file
//	sourceFile = File.createTempFile("sampleFile", ".txt");
//	System.out.println("Creating a sample file at: " + sourceFile.toString());
//	Writer output = new BufferedWriter(new FileWriter(sourceFile));
//	output.write("Hello Azure!");
//	output.close();
//
//	//Getting a blob reference
//	CloudBlockBlob blob = container.getBlockBlobReference(sourceFile.getName());
//
//	//Creating blob and uploading file to it
//	System.out.println("Uploading the sample file ");
//	blob.uploadFromFile(sourceFile.getAbsolutePath());
//	
//	//3、列出容器中的 Blob
//	//可以使用 CloudBlobContainer.ListBlobs 获取容器中的文件列表。 下面的代码检索 blob 列表，然后循环访问它们，显示找到的 blob 的 URI。 可以从命令窗口中复制 URI，然后将其粘贴到浏览器以查看文件。
//	for (ListBlobItem blobItem : container.listBlobs()) {
//	    System.out.println("URI of blob is: " + blobItem.getUri());
//	}
//	
//	//4、下载 Blob
//	//以下代码下载上一部分上传的 blob，对 blob 名称添加“_DOWNLOADED”后缀，以便可以在本地磁盘上看到两个文件。
//	
//	// Download blob. In most cases, you would have to retrieve the reference
//	// to cloudBlockBlob here. However, we created that reference earlier, and 
//	// haven't changed the blob we're interested in, so we can reuse it. 
//	// Here we are creating a new file to download to. Alternatively you can also pass in the path as a string into downloadToFile method: blob.downloadToFile("/path/to/new/file").
//	downloadedFile = new File(sourceFile.getParentFile(), "downloadedFile.txt");
//	blob.downloadToFile(downloadedFile.getAbsolutePath());
//	
//	//5、清理资源
//	//如果不再需要在此快速入门中上传的 blob，可使用 CloudBlobContainer.DeleteIfExists 删除整个容器。 此操作也会删除容器中的文件。
//	
//	
}