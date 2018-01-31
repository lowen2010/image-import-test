package demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * 通过excel导入图片测试类
 * @author chengw
 *
 */
public class ImageImportByExcelTest {
	/**
	 * 获取图片和位置 (xls)
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public static Map<String, HSSFPictureData> getPictures (HSSFSheet sheet) throws IOException {
	    Map<String, HSSFPictureData> map = new HashMap<String, HSSFPictureData>();
	    List<HSSFShape> list = sheet.getDrawingPatriarch().getChildren();
	    for (HSSFShape shape : list) {
	        if (shape instanceof HSSFPicture) {
	            HSSFPicture picture = (HSSFPicture) shape;
	            HSSFClientAnchor cAnchor = (HSSFClientAnchor)picture.getAnchor();
	            HSSFPictureData pdata = picture.getPictureData();
	            String key = cAnchor.getRow1() + "-" + cAnchor.getCol1(); // 行号-列号
	            map.put(key, pdata);
	        }
	    }
	    return map;
	}
	
	/**
	 * 保存图片
	 * @param data
	 * @param name
	 * @param savePath
	 */
	public static void savePicture(HSSFPictureData data,String name,String savePath){
		String ext = data.suggestFileExtension();
		StringBuilder fullSavePath = new StringBuilder();
		fullSavePath.append(savePath);
		fullSavePath.append("/");
		fullSavePath.append(name);
		switch (ext) {
		case "jpeg":
			fullSavePath.append(".jpg");
			break;
		default:
			fullSavePath.append(".jpg");
			break;
		}
		OutputStream out;
		try {
			out = new FileOutputStream(fullSavePath.toString());
			out.write(data.getData());
			System.out.println("保存图片成功："+fullSavePath.toString());
			out.close();
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 检查文件夹是否存在，不存在则新建
	 * @param savePath
	 */
	public static void checkDirectory(String savePath) {
		File file = new File(savePath);
		if(!file.isDirectory() && !file.exists()) {
			file.mkdir();
			System.out.println("新建保存目录:"+savePath);
		}
	}
	
	public static void main(String[] args) throws Exception{
		//图片保存路径
		String savePath = "C:/Users/Administrator/Desktop/imagesSave";
		checkDirectory(savePath);
		//加载excel数据
		InputStream is = ImageImportByExcelTest.class.getClassLoader().getResourceAsStream("data/importImage.xls");
		HSSFWorkbook workbook = (HSSFWorkbook) WorkbookFactory.create(is); 
		HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
		//缓存全部图片到map中
		Map<String, HSSFPictureData> map = getPictures(sheet);
		for (Row row : sheet) {
			int rowNo = row.getRowNum();
			if(rowNo>0){
				String policyNo= row.getCell(0).getStringCellValue();
				savePicture(map.get(rowNo+"-"+"1"),policyNo,savePath);
			}
		}
		is.close();
	}

}
