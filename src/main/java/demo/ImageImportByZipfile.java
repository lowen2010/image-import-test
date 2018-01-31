package demo;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 通过zip file导入图片测试类
 * @author chengw
 *
 */
public class ImageImportByZipfile {
	/**
	 * 解析zip文件
	 * @param zipFile
	 * @param savePath
	 */
	public static void readZipFile(String zipFile,String savePath) {
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile),Charset.forName("gbk"));
            ZipFile zf = new ZipFile(zipFile);
            ZipEntry zipEntry = null;
            while ((zipEntry = zin.getNextEntry()) != null) {
            	if(!zipEntry.isDirectory()){
            		String fileName = zipEntry.getName().substring(zipEntry.getName().indexOf("/"));
            		String fullFileName = savePath + fileName;
            		System.out.println("生成文件路径:"+fullFileName);
                    InputStream zipStream = zf.getInputStream(zipEntry);
                    saveFile(zipStream,fullFileName);
                    zipStream.close();
            	}
            }
            zf.close();
            zin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 保存文件到指定路径
	 * @param in	
	 * @param fileName 文件路径
	 */
	public static void saveFile(InputStream in,String fileName){
		try {
			OutputStream out = new FileOutputStream(fileName);
			int legth = 0;
			byte[] buffer = new byte[1024];
			while((legth=in.read(buffer))!=-1){
				out.write(buffer, 0, legth);
			}
			out.close();
			out.flush();
			System.out.println("保存文件【"+ fileName +"】成功");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		String filePath = ImageImportByZipfile.class.getClassLoader().getResource("data/upload.zip").getPath();
		String savePath = "C:/Users/Administrator/Desktop/imageZip";
		ImageImportByExcelTest.checkDirectory(savePath);
		readZipFile(filePath,savePath);
	}
}
