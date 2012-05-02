/**
 * 
 */
package haframework.draw;

import haframework.file.FileManager;

import java.io.InputStream;
import java.util.HashMap;

/**
 * @author hjb
 *
 */
public class Font
{
	//--------------------------------------------- private member ---------------------------------------------------
	
	protected Sprite m_texture = null;
	protected CommonInfo m_commonInfo = null;
	protected HashMap<Integer,CharInfo> m_charInfoMap = null;
	
	//--------------------------------------------- public function --------------------------------------------------

	/**
	 * @desc	constructor
	 */
	public Font()
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @desc	constructor
	 * @param	fontFile
	 * @param	texRes
	 */
	public Font( String fontFile, int texRes )
	{
		InputStream inputStream = FileManager.Singleton().OpenFile( fontFile );
		
		analysisFnt( inputStream );

		try
		{
			inputStream.close();
		}
		catch( Exception e )
		{
			//TODO
		}
		
		m_texture = SpriteFactory.Singleton().CreateSprite( texRes );
	}

	/**
	 * @desc	draw the text
	 * @param	text
	 * @param	x
	 * @param	y
	 */
	public void DrawText( String text, float x, float y, float scale, Color color )
	{
		int len = text.length();
		
		float curX = x;
		float curY = y;
		CharInfo cf;
		
		for( int i = 0; i < len; i++ )
		{
			char chr = text.charAt(i);
			int charIdx = (int)chr;
			cf = m_charInfoMap.get( charIdx );
			
			if( chr == '\n' )
			{
				curX = x;
				curY = m_commonInfo._lineHeight * scale;
			}
			else
			{
				if( cf == null )
				{
					//can not find this character
					curX += 10;
					continue;
				}
				
				m_texture.SetUV( cf._x, cf._y, cf._w, cf._h );
				m_texture.Draw( curX + cf._xoffset, curY + cf._yoffset, cf._w * scale, cf._h * scale );
			}
			
			curX += cf._xadvance * scale;
		}
	}

	/**
	 * @desc	draw the text
	 * @param	text
	 * @param	x
	 * @param	y
	 * @param	color
	 * @param	rotate
	 */
	public void DrawText( String text, float x, float y, Color color, float rotate )
	{
		// TODO Auto-generated method stub
	}
	
	/**
	 * @desc	release this font
	 */
	public void Release()
	{
		//TODO
	}
	
	//--------------------------------------------- private function --------------------------------------------------
	
	// prase the fnt file
	private void analysisFnt(InputStream inputStream)
	{
		m_commonInfo = new CommonInfo();
		m_charInfoMap = new HashMap<Integer,CharInfo>();
		
		try
		{
			byte intNum[] = new byte[4];
			
			byte bmfVersion[] = new byte[4];
			inputStream.read( bmfVersion, 0, 4 );
			if( 66 != bmfVersion[0] && 77 != bmfVersion[1] && 70 != bmfVersion[2] )
			{
				throw new Error( "[Error]:  This is not a legal BMF file" );
			}
			if( bmfVersion[3] != 3 )
			{
				throw new Error( "[Error]:	Version 3 needed" );
			}
			
			// block type 1: info
			byte blockType[] = new byte[1];
			byte blockSize[] = new byte[4];
			inputStream.read( blockType, 0, 1 );
			inputStream.read( blockSize, 0, 4 );
			int blockSizeInt = byteArrayToInt( blockSize );
			inputStream.skip( blockSizeInt );
			
			// block type 2: common
			inputStream.read( blockType, 0, 1 );
			inputStream.read( blockSize, 0, 4 );
			blockSizeInt = byteArrayToInt( blockSize );
			byte shortNum[] = new byte[2];
			inputStream.read( shortNum, 0, 2 );
			m_commonInfo._lineHeight = byteArrayToShort( shortNum );
			inputStream.read( shortNum, 0, 2 );
			m_commonInfo._base = byteArrayToShort( shortNum );
			inputStream.read( shortNum, 0, 2 );
			m_commonInfo._textureW = byteArrayToShort( shortNum );
			inputStream.read( shortNum, 0, 2 );
			m_commonInfo._textureH = byteArrayToShort( shortNum );
			inputStream.read( shortNum, 0, 2 );
			m_commonInfo._numPages = byteArrayToShort( shortNum );
			byte charBuff[] = new byte[1];
			inputStream.read( charBuff, 0, 1 );
			m_commonInfo._bitField = (char)charBuff[0];
			inputStream.read( charBuff, 0, 1 );
			m_commonInfo._alphaChannel = (char)charBuff[0];
			inputStream.read( charBuff, 0, 1 );
			m_commonInfo._redChannel = (char)charBuff[0];
			inputStream.read( charBuff, 0, 1 );
			m_commonInfo._greenChannel = (char)charBuff[0];
			inputStream.read( charBuff, 0, 1 );
			m_commonInfo._blueChannel = (char)charBuff[0];
			
			// block type 3: pages
			inputStream.read( blockType, 0, 1 );
			inputStream.read( blockSize, 0, 4 );
			blockSizeInt = byteArrayToInt( blockSize );
			//[hack]	because all the fnt be used in this game only have one page ( texture )
			inputStream.skip( blockSizeInt );
			
			// block type 4: chars
			inputStream.read( blockType, 0, 1 );
			inputStream.read( blockSize, 0, 4 );
			blockSizeInt = byteArrayToInt( blockSize );
			int numChars = blockSizeInt / 20;
			CharInfo cf = null;
			for( int i = 0; i < numChars; i++ )
			{
				cf = new CharInfo();
				
				inputStream.read( intNum, 0, 4 );
				cf._id = byteArrayToInt( intNum );
				inputStream.read( shortNum, 0, 2 );
				cf._x = byteArrayToShort( shortNum );
				inputStream.read( shortNum, 0, 2 );
				cf._y = byteArrayToShort( shortNum );
				inputStream.read( shortNum, 0, 2 );
				cf._w = byteArrayToShort( shortNum );
				inputStream.read( shortNum, 0, 2 );
				cf._h = byteArrayToShort( shortNum );
				inputStream.read( shortNum, 0, 2 );
				cf._xoffset = byteArrayToShort( shortNum );
				inputStream.read( shortNum, 0, 2 );
				cf._yoffset = byteArrayToShort( shortNum );
				inputStream.read( shortNum, 0, 2 );
				cf._xadvance = byteArrayToShort( shortNum );
				inputStream.read( charBuff, 0, 1 );
				cf._page = (char)charBuff[0];
				inputStream.read( charBuff, 0, 1 );
				cf._chnl = (char)charBuff[0];
				
				m_charInfoMap.put( cf._id, cf );
			}
		}
		catch( Exception e )
		{
			System.out.println( e.toString() );
		}
	}
	
	// convert byte array to short
	private short byteArrayToShort( byte[] b )
	{
		short num = 0;
		
		short s0 = (short)( b[0] & 0xff );
		short s1 = (short)( b[1] & 0xff );
		s1 <<= 8;
		num = (short)( s0 | s1 );
		
		return num;
	}
	
	// convert byte array to integer
	private int byteArrayToInt( byte[] b ) 
	{
		int num = 0;
		
        int s0 = b[0] & 0xff;
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        num = s0 | s1 | s2 | s3;
        
        return num;
    }

}

class CharInfo
{
	public int _id;
	public short _x;
	public short _y;
	public short _w;
	public short _h;
	public short _xoffset;
	public short _yoffset;
	public short _xadvance;
	public char _page;
	public char _chnl;
}

class CommonInfo
{
	public short _lineHeight;
	public short _base;
	public short _textureW;
	public short _textureH;
	public short _numPages;
	
	public char _bitField;
	public char _alphaChannel;
	public char _redChannel;
	public char _greenChannel;
	public char _blueChannel;
}
