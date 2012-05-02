/**
 * 
 */
package haframework.draw;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * @author hjb
 *
 */
public class RenderList
{
	static public final int MAX_VERTEX_NUM = 1024;
	static public final int MAX_INDEX_NUM = 2048;
	static public final int MAX_TEXTURE_NUM = 32;
	
	static public final int FLAG_BITMAP_TEX = -1;
	
	//----------------------------------------------------- data --------------------------------------------------------
	
	static protected HashMap<Integer,TextureInfo> m_textureMap = null;
	static protected int[] m_textures = null;
	static protected int m_textureNum = 0;
	static protected Vector<TextureInfo> m_pedingTextures = null;
	
	static protected FloatBuffer m_vertexBuffer = null;
	static protected FloatBuffer m_colorBuffer = null;
	static protected FloatBuffer m_uvBuffer = null;
	static protected ShortBuffer m_indexBuffer = null;
	
	static protected int m_curTexRes = 0;
	static protected Vector<RenderBlock> m_renderBlocks = null;
	static protected int m_curRenderBlockIndex = -1;
	static protected int m_spriteNum = 0;
	static protected int m_renderBlockNum;
	static protected float m_depth = 0.0f;
	
	//--------------------------------------------------- function ------------------------------------------------------
	
	/**
	 * @desc	initial the render list
	 */
	static public void Initial( GL10 gl )
	{
		m_textureMap = new HashMap<Integer,TextureInfo>();
		m_renderBlocks = new Vector<RenderBlock>();
		m_pedingTextures = new Vector<TextureInfo>();
		
		// create buffer
		ByteBuffer vb = ByteBuffer.allocateDirect( MAX_VERTEX_NUM * 3 * 4 );
		vb.order( ByteOrder.nativeOrder() );
		m_vertexBuffer = vb.asFloatBuffer();
		
		ByteBuffer cb = ByteBuffer.allocateDirect( MAX_VERTEX_NUM * 4 * 4 );
		cb.order( ByteOrder.nativeOrder() );
		m_colorBuffer = cb.asFloatBuffer();
		
		ByteBuffer ub = ByteBuffer.allocateDirect( MAX_VERTEX_NUM * 2 * 4 );
		ub.order( ByteOrder.nativeOrder() );
		m_uvBuffer = ub.asFloatBuffer();

		ByteBuffer ib = ByteBuffer.allocateDirect( MAX_INDEX_NUM * 2 );
		ib.order( ByteOrder.nativeOrder() );
		m_indexBuffer = ib.asShortBuffer();
		
		m_textures = new int[MAX_TEXTURE_NUM];
		gl.glGenTextures( MAX_TEXTURE_NUM, m_textures, 0 );
		
	}
	
	/**
	 * @desc	judge if a texture exist or not
	 * @param	texRes
	 * @return
	 */
	static public boolean IsTextureExist( int texRes )
	{
		if( m_textureMap.get( texRes ) == null )
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * @desc	create a texture
	 * @param	texRes
	 * @param	wid
	 * @param	hei
	 * @return
	 */
	static public boolean CreateTexture( int texRes, Bitmap bmp, int wid, int hei )
	{
		if( m_textureNum >= MAX_TEXTURE_NUM )
		{
			System.out.println( "[Error]: Max texture number" );
			return false;
		}
		
		TextureInfo info = new TextureInfo();
		info._index = m_textureNum;
		info._width = wid;
		info._height = hei;
		info._bmp = bmp;
		m_textureMap.put( texRes, info );
		m_textureNum++;
		
		m_pedingTextures.add( info );
		
		return true;
	}
	
	/**
	 * @desc	clear all the texture ( be careful to use this interface )
	 */
	static public void CleanTexture()
	{
		//TODO
		
		m_textureNum = 0;
	}
	
	/**
	 * @desc	return the width of specific texture
	 * @param	texRes
	 * @return
	 */
	static public int GetTextureWidth( int texRes )
	{
		TextureInfo info = m_textureMap.get( texRes );
		
		if( info == null )
		{
			return 0;
		}
		
		return info._width;
	}
	
	/**
	 * @desc	return the height of specific texture
	 * @param	texRes
	 * @return
	 */
	static public int GetTextureHeight( int texRes )
	{
		TextureInfo info = m_textureMap.get( texRes );
		
		if( info == null )
		{
			return 0;
		}
		
		return info._height;
	}
	
	/**
	 * @desc	add a sprite to the renderlist
	 * @param	spr
	 */
	static private short[] indexs = {0,0,0,0,0,0};
	static private float[] vertexs = {0,0,0,0,0,0,0,0,0,0,0,0};
	static private float[] colors = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	static private float[] uvs = {0,0,0,0,0,0,0,0};
	static public void AddSprite( Sprite spr )
	{
		int texRes = spr.GetTexRes();
		RenderBlock rb = null;
		
		// create new render block
		if( texRes != m_curTexRes )
		{
			m_curRenderBlockIndex++;
			m_renderBlockNum++;
			
			rb = new RenderBlock();
			rb._textureId = m_textures[m_textureMap.get( texRes )._index];
			rb._indexOffset = m_indexBuffer.position();
			rb._vertexNum = 0;
			m_renderBlocks.add( rb );
			
			m_curTexRes = texRes;
		}
		
		int curVertexPos = m_vertexBuffer.position() / 3;
		indexs[0] = (short)curVertexPos;
		indexs[1] = (short)( curVertexPos + 2 );
		indexs[2] = (short)(curVertexPos + 1);
		indexs[3] = (short)curVertexPos;
		indexs[4] = (short)(curVertexPos + 3);
		indexs[5] = (short)(curVertexPos + 2);
		m_indexBuffer.put( indexs );
		
		vertexs[0] = spr._x1;			vertexs[1] = spr._y1;			vertexs[2] = m_depth;
		vertexs[3] = spr._x2;			vertexs[4] = spr._y2;			vertexs[5] = m_depth;
		vertexs[6] = spr._x3;			vertexs[7] = spr._y3;			vertexs[8] = m_depth;
		vertexs[9] = spr._x4;			vertexs[10] = spr._y4;			vertexs[11] = m_depth;
		m_vertexBuffer.put( vertexs );
		
		uvs[0] = spr._u1;			uvs[1] = spr._v1;
		uvs[2] = spr._u2;			uvs[3] = spr._v1;
		uvs[4] = spr._u2;			uvs[5] = spr._v2;
		uvs[6] = spr._u1;			uvs[7] = spr._v2;
		m_uvBuffer.put( uvs );
		
		colors[0] = spr._col1_r;	colors[1] = spr._col1_g;	colors[2] = spr._col1_b;	colors[3] = spr._col1_a;
		colors[4] = spr._col2_r;	colors[5] = spr._col2_g;	colors[6] = spr._col2_b;	colors[7] = spr._col2_a;
		colors[8] = spr._col3_r;	colors[9] = spr._col3_g;	colors[10] = spr._col3_b;	colors[11] = spr._col3_a;
		colors[12] = spr._col4_r;	colors[13] = spr._col4_g;	colors[14] = spr._col4_b;	colors[15] = spr._col4_a;
		m_colorBuffer.put( colors );
		
		rb = m_renderBlocks.lastElement();
		rb._vertexNum += 6;									// two trangles for one rectangle
		
		m_depth -= 0.01f;
		m_spriteNum++;
	}
	
	/**
	 * @desc	increase depth
	 */
	static public void AddDepth( float val )
	{
		m_depth += val;
	}
	
	/**
	 * @desc	clear the renderlist
	 */
	static public void Clear()
	{	
		m_vertexBuffer.clear();
		m_colorBuffer.clear();
		m_uvBuffer.clear();
		m_indexBuffer.clear();
		m_renderBlocks.clear();
		m_renderBlockNum = 0;
		m_spriteNum = 0;
		m_curRenderBlockIndex = -1;
		m_curTexRes = 0;
		m_depth = 90.0f;
	}
	
	/**
	 * @desc	render the graphic
	 * @param	gl
	 */
	static public void Render( GL10 gl )
	{
		createPendingTextures( gl );
		
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
		
		m_vertexBuffer.position( 0 );
		m_colorBuffer.position( 0 );
		m_uvBuffer.position( 0 );
		m_indexBuffer.position( 0 );
		
		gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, m_vertexBuffer );
		gl.glColorPointer( 4, GL10.GL_FLOAT, 0, m_colorBuffer );
		gl.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, m_uvBuffer );
		
		int i;
		RenderBlock rb;
		for( i = 0; i < m_renderBlocks.size(); i++ )
		{
			rb = m_renderBlocks.get( i );
			
			gl.glBindTexture( GL10.GL_TEXTURE_2D, rb._textureId );
			m_indexBuffer.position( rb._indexOffset );
			gl.glDrawElements( GL10.GL_TRIANGLES, rb._vertexNum, GL10.GL_UNSIGNED_SHORT, m_indexBuffer );
		}
	}
	
	/**
	 * @desc	destory the renderList
	 */
	public static void Destory()
	{
		m_textureMap = null;
		m_renderBlocks = null;
		m_pedingTextures = null;
		m_vertexBuffer = null;
		m_colorBuffer = null;
		m_indexBuffer = null;
		m_uvBuffer = null;
		m_textures = null;
	}
	
	//--------------------------------------------------- private function ------------------------------------------------------
	
	// create the pending textures
	static protected void createPendingTextures( GL10 gl )
	{
		int textureId;
		
		TextureInfo ti;
		for( int i = 0; i < m_pedingTextures.size(); i++ )
		{
			ti = m_pedingTextures.get( i );
			
			textureId = m_textures[ti._index];
			gl.glBindTexture( GL10.GL_TEXTURE_2D, textureId );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE );
			gl.glTexEnvf( GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE );
			
			GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, ti._bmp, 0 );
			ti._bmp.recycle();
			ti._bmp = null;
		}		
		
		m_pedingTextures.clear();
	}
	
}

class TextureInfo
{
	public int _index;
	public int _width;
	public int _height;
	public Bitmap _bmp;
}

class RenderBlock
{
	public int _textureId;
	public int _indexOffset;
	public int _vertexNum;
}

