package aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class S3FileReader {



    public void downloadConfigFromS3(String filePath){
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        String bucketName = System.getenv("BUCKET_NAME");
        String key = System.getenv("KEY_NAME");

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_WEST_2)
                .build();


        S3Object s3Object = s3Client.getObject(bucketName, key);
        String fileName = s3Object.getKey();

        InputStream inputStream = s3Object.getObjectContent();

        try{
            OutputStream outputStream = new FileOutputStream(filePath + fileName);

            IOUtils.copy(inputStream, outputStream);

            System.out.println("File downloaded to: " + filePath + fileName);

            inputStream.close();
            outputStream.close();

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }




}
