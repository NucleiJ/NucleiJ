package at.ac.htlhl.nucleij.util;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

/**
 * A file filter that accepts file names with a specific suffix.
 *
 */
public class SuffixFileFilter extends FileFilter
{
	private String acceptingSuffix;
	private String description;
	private boolean acceptDir;

	/**
	 * Creates a new instance of SuffixFileFilter.
	 *
	 * @param acceptingSuffix
	 * @param description
	 */
	public SuffixFileFilter(String acceptingSuffix, String description)
	{
		this(acceptingSuffix, description, true);
	}

	/**
	 * Creates a new instance of SuffixFileFilter.
	 *
	 * @param acceptingSuffix
	 * @param description
	 * @param acceptDir
	 */
	public SuffixFileFilter(String acceptingSuffix, String description, boolean acceptDir)
	{
		this.acceptingSuffix = acceptingSuffix;
		this.description = description;
		this.acceptDir = acceptDir;
	}

	/**
	 * Returns true if the specified File does not represent a directory and 
	 * its filename extension matches this SuffixFileFilter's accepting suffix 
	 * or the accepting suffix' length is 0.
	 * 
	 * @param file  the File to check
	 * @return true if the specified File does not represent a directory and 
	 *         its filename extension matches this SuffixFileFilter's accepting
	 *         suffix or the accepting suffix' length is 0.
	 */	
	@Override
	public boolean accept(File file)
	{
		if (this.acceptDir && file.isDirectory() || this.acceptingSuffix.length() == 0)
			return true;		
		
		String s = file.getName().toLowerCase(Locale.ENGLISH);
		if(s.endsWith("."+this.acceptingSuffix))
			return true;
		
		return false;
	}

	public String getAcceptingSuffix()
	{
		return this.acceptingSuffix;
	}

	@Override
	public String getDescription()
	{
		return this.description;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.acceptingSuffix == null) ? 0 : this.acceptingSuffix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SuffixFileFilter other = (SuffixFileFilter) obj;
		if (this.acceptingSuffix == null)
		{
			if (other.acceptingSuffix != null)
				return false;
		}
		else if (!this.acceptingSuffix.equals(other.acceptingSuffix))
			return false;
		return true;
	}

	
	
	@Override
	public String toString()
	{
		return "SuffixFileFilter: " + this.acceptingSuffix;
	}
}
