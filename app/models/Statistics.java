package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import play.Logger;
import play.db.ebean.Model;

@Entity
public class Statistics extends Model{

	@Id
	public int id;
	
	@OneToOne(mappedBy="statistic")
	public Restaurant restaurant;
	
	public int numberOfVisits;
	
	public int itemsBought;

	
	static final String statsFilePath = "./statistic/admin_statistic/";
	static Finder<Integer, Statistics> find = new Finder<Integer, Statistics>(Integer.class, Statistics.class);

	
	public Statistics(Restaurant restaurant){
		this.restaurant= restaurant;
		this.numberOfVisits = 0;
		this.itemsBought = 0;
	}
	
	public static Statistics createStatistics(Restaurant restaurant){
		Statistics statistic = new Statistics(restaurant);
		statistic.save();
		return statistic;
	}
	
	public static void resetStatistic(Restaurant restaurant){
		Statistics stats = find.where().eq("restaurant", restaurant).findUnique();
		stats.numberOfVisits = 0;
		stats.itemsBought = 0;
		stats.save();
	}
	
	public static List<Statistics> all(){
		List<Statistics> all = find.all();
		if(all == null)
			all = new ArrayList<Statistics>();
		return all;
	}
	
	public void visited(){
		this.numberOfVisits++;
		this.update();		
	}
	
	public void itemsBought(int quantity) {
		this.itemsBought += quantity;
		this.update();	
		
	}
	
	public static File createStatisticsFile(){
		//TODO WHOLE APP TO USE ONE HREF.
		final String href = "http://localhost:9000/";
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet =  workbook.createSheet("Statistics"); 
	        String fileName = UUID.randomUUID().toString().replace("-", "") + ".xls";
	        //CREATING FOLDER FOR STATISTICS                
	       boolean isMade = new File(statsFilePath).mkdirs();
	        File statistic = new File(statsFilePath +fileName);
	        List<Statistics> all = find.all();
	        System.out.println("Is made: " +isMade);
	        //SETTING STYLE
	        HSSFCellStyle style = workbook.createCellStyle();
	        style.setBorderTop((short) 6); 
	        style.setBorderBottom((short) 1); 
	        style.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);
	        style.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	        style.setFillPattern(HSSFColor.GREY_80_PERCENT.index);
	        
	        HSSFFont font = workbook.createFont();
	        font.setFontName(HSSFFont.FONT_ARIAL);
	        font.setFontHeightInPoints((short) 10);
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        font.setColor(HSSFColor.WHITE.index);	        
	        style.setFont(font);
	        style.setWrapText(true);
	        
	        
	        //HARDCODED CELLS and added style !
	        HSSFRow rowhead=   sheet.createRow((short)0);	       
	        HSSFCell couponId = rowhead.createCell(0);
	        couponId.setCellValue(new HSSFRichTextString("Coupon id"));
	        couponId.setCellStyle(style);
	       
	        HSSFCell couponName = rowhead.createCell(1);
	        couponName.setCellValue(new HSSFRichTextString("Coupon name"));
	        couponName.setCellStyle(style);
	        
	        HSSFCell  visited = rowhead.createCell(2);
	        visited.setCellValue(new HSSFRichTextString("Visited"));
	        visited.setCellStyle(style);
	        
	        HSSFCell bought = rowhead.createCell(3);
	        bought.setCellValue(new HSSFRichTextString("Bought"));
	        bought.setCellStyle(style); 
	        
	        rowhead.setHeight((short) 0);
	        //Creating rows for each statistic. 
	        //Setting hyperlinks on name.
	        int rowIndex = 1;
	        HSSFCell temp;	      
	        for(Statistics stat: all){
	        	HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
	        	HSSFRow row=   sheet.createRow(rowIndex);
	        	row.createCell(0).setCellValue(stat.restaurant.id);
	            temp = row.createCell(1);
	            temp.setCellValue(stat.restaurant.name);
	            link.setAddress(href + "coupon/"+stat.restaurant.id);
	            temp.setHyperlink(link);
	            row.createCell(2).setCellValue(stat.numberOfVisits);
	            row.createCell(3).setCellValue(stat.itemsBought); 	            
	            rowIndex++;
	        }
	        
	        //rowhead.setRowStyle(style);
	        //auto size columns
	        for(int i=0; i<4; i++){
	        	 sheet.autoSizeColumn((short) i);	        	 
	        }	       
	       
	        FileOutputStream fileOut = new FileOutputStream(statistic);
	        workbook.write(fileOut);
	        fileOut.flush();
	        fileOut.close();
	        workbook.close();
	        System.out.println("Your excel file has been generated!");	
	        
	        return statistic;
		} catch (FileNotFoundException e) {
			Logger.error("Statistic file exception ", e );			
		} catch (IOException e) {
			Logger.error("IO exception", e);
		}  
		return null;
	}
	
}
