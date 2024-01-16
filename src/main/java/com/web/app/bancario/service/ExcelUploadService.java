//package com.web.app.bancario.service;
//
//import java.io.IOException;
//import java.io.InputStream;
////import java.time.LocalDate;
////import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Objects;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.web.app.bancario.entity.Usuario;
//
//@Service
//public class ExcelUploadService {
//
//	
//	public static boolean isValidExcelFile(MultipartFile file) {
//		
//		return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//	}
//
//	public static List<Usuario>getUserDataFromExcel(InputStream inputStream){
//		List<Usuario>usuarios=new ArrayList<>();
//		XSSFWorkbook worbook=null;
//		try {
//			worbook=new XSSFWorkbook(inputStream);
//			XSSFSheet sheet=worbook.getSheet("usuarios");
//			System.out.println("en el try de getMedicametosDataFromExcel");
//			int rowIndex=0;
//			for(Row row:sheet) {
//				if(rowIndex==0) {
//					rowIndex++;
//					continue;
//				}
//				Iterator<Cell>cellIterator=row.iterator();
//				int cellIndex=0;
//				Usuario usuario=new Usuario();
//				while (cellIterator.hasNext()) {
//					System.out.println("dentro del wile");
//					Cell cell=cellIterator.next();
//					switch (cellIndex) {
//					
//					
//					case 0->usuario.setNombre(cell.getStringCellValue());
//					case 1->usuario.setApellido(cell.getStringCellValue());
//					case 2->usuario.setTelefono(String.valueOf(cell.getNumericCellValue()));
//					case 3->usuario.setCorreo(cell.getStringCellValue());
//					case 4->usuario.setContrasenia(String.valueOf(cell.getNumericCellValue()));
//					case 5->usuario.setSaldo((int)cell.getNumericCellValue());
//					default->{
//						
//					}
//
//					}
//					cellIndex++;
//					
//				}
//				usuarios.add(usuario);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return usuarios;
//	}
//
//}
