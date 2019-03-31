package net.cmono.dtos;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtistFilenameFilter implements FilenameFilter{  
     
    public boolean accept(File dir,String name){  
    	Pattern pattern = Pattern.compile("\\d+");
    	Matcher matcher = pattern.matcher(name);
		boolean tem = matcher.matches();
        return tem;  
    }  
}
