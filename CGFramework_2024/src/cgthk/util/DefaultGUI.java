package cgthk.util;

import org.lwjgl.*;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.*;

import cgthk.math.Vec4;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.*;

import javax.imageio.stream.IIOByteBuffer;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.*;


/**
 * The Default Gui. Responsible for all standard GUI elements as well as calling the customGui
 * function of the sandbox.
 * 
 * @author Mario
 * @author Arnulph
 */
public class DefaultGUI {
	
    // General
    private NkContext ctx;
    private NuklearCallback nCb;
    
    // Fps viewer
    public String fpsString = "0.0";
    public boolean vsync = true;
    
    // Help Button
    public boolean showHelp = false;
    
    // Grid Toggle
    public ByteBuffer grid = BufferUtils.createByteBuffer(1).put(0, (byte) 1);
    
    // Color Picker
    private NkColorf colorPickerColor = NkColorf.create();
    
    public DefaultGUI( NuklearCallback nCb, NkContext ctx ) {
    	this.ctx = ctx;
    	this.nCb = nCb;
    }

    public void layout( int x, int y, String header, int guiWidth, int guiHeight ) throws URISyntaxException {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.mallocStack(stack);
            if (nk_begin(
                ctx,
                header,
                nk_rect(x, y, guiWidth, guiHeight, rect),
                NK_WINDOW_BORDER | NK_WINDOW_TITLE 
            )) {
            	/*
            	 * Default GUI
            	 */
            	nk_layout_row_dynamic(ctx, 30, 2);
            	nk_label(ctx, fpsString, NK_LEFT);
            	String syncstring = vsync ? "ON" : "OFF";
            	nk_label( ctx, "V-SYNC: " + syncstring, NK_LEFT );
            	
                nk_layout_row_static(ctx, 30, 100, 2);
                if (nk_button_label(ctx, "Screenshot")) {
                    nCb.takeScreenshot();
                }
                if (nk_button_label(ctx, "Show Help")) {
                    showHelp = !showHelp;
                }
                
                nk_layout_row_dynamic(ctx, 30, 1);
                nk_checkbox_label(ctx, "Draw Floor Grid", grid );   
                
                
                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "Object Color:", NK_TEXT_LEFT);
                
                // color combo box (write only)
                nk_layout_row_dynamic(ctx, 25, 1);
            	NkColor col = NkColor.create();
            	col.r((byte)(colorPickerColor.r() * 255));                
                col.g((byte)(colorPickerColor.g() * 255));
                col.b((byte)(colorPickerColor.b() * 255));
                col.a((byte)(colorPickerColor.a() * 255));
                if (nk_combo_begin_color(ctx, col, NkVec2.mallocStack(stack).set(nk_widget_width(ctx), 400)))
                {
                    // color picker
                    nk_layout_row_dynamic(ctx, 130, 1);
                    nk_color_picker(ctx, colorPickerColor, NK_RGBA);

                    // color slider
                    nk_layout_row_dynamic(ctx, 25, 1);
                    colorPickerColor.r(nk_propertyi(ctx, "#R:", 0, (int)(colorPickerColor.r() * 255), 255, 1, 1) / 255.0f);
                    colorPickerColor.g(nk_propertyi(ctx, "#G:", 0, (int)(colorPickerColor.g() * 255), 255, 1, 1) / 255.0f);
                    colorPickerColor.b(nk_propertyi(ctx, "#B:", 0, (int)(colorPickerColor.b() * 255), 255, 1, 1) / 255.0f);
                    colorPickerColor.a(nk_propertyi(ctx, "#A:", 0, (int)(colorPickerColor.a() * 255), 255, 1, 1) / 255.0f);
                    nk_combo_end(ctx);
                }
                
                // User properties
                nk_layout_row_dynamic(ctx, 30, 1);
                nCb.drawCustomGUI( ctx );               
                
                // Add CGLearning Link
                nk_layout_row_dynamic(ctx, 30, 1);
                URI uri = new URI("http://cg.web.th-koeln.de/cg-learning/index.php");
                if( nk_button_label(ctx, "CGLearning")){
                		open(uri);
                }
            }
            nk_end(ctx);            
        }
    }
    
    public void setColor( Vec4 color ){    	
    	colorPickerColor.r(color.x);
    	colorPickerColor.g(color.y);
    	colorPickerColor.b(color.z);
    	colorPickerColor.a(color.w);
    }
    
    public Vec4 getColor(){
    	return new Vec4(colorPickerColor.r(), colorPickerColor.g(), colorPickerColor.b(), colorPickerColor.a());
    }

    /**
     * Open specified uri 
     */
    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
          try {
            Desktop.getDesktop().browse(uri);
          } catch (IOException e) { e.printStackTrace(); }
        } else { System.out.println("Desktop not supported"); }
      }
}
