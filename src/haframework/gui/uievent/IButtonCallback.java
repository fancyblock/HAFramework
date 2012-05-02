/**
 * 
 */
package haframework.gui.uievent;

import haframework.gui.UIButton;

/**
 * @author hjb
 *
 */
public interface IButtonCallback
{
	void onButtonDown( UIButton btn );
	
	void onButtonUp( UIButton btn );
	
	void onButtonClick( UIButton btn );
}
