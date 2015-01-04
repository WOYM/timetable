package org.woym.logic.spec;
import java.io.File;


/**
 * 
 * @author joho
 *
 */
public interface IDocuGenerator {
	
	public File generateTxt();
	
	public File generateCsv();
	
	public File generatePdf();
}
