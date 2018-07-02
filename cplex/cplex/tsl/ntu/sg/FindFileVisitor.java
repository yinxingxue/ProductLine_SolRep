/**
 * 
 */
package cplex.tsl.ntu.sg;
 

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

 /**
  * 
  * @author yinxing
  *
  */
public class FindFileVisitor extends SimpleFileVisitor<Path> {

	private List<String> filenameList = new ArrayList<String>();

	private String fileSuffix = null;

	public FindFileVisitor(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

		if (file.toString().endsWith(fileSuffix)) {
			filenameList.add(file.toString());
		}
		return FileVisitResult.CONTINUE;
	}

	public List<String> getFilenameList() {
		return filenameList;
	}

	public void setFilenameList(List<String> filenameList) {
		this.filenameList = filenameList;
	}


}