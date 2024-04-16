package cgthk.util;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.stackPush;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;

/**
 * Small Help Window to show controls.
 * 
 * @author Mario
 *
 */
public class HelpGUI {

	
    public HelpGUI() {
    }

    public void layout(NkContext ctx, int x, int y, int guiWidth, int guiHeight ){
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.mallocStack(stack);

            if (nk_begin(
                ctx,
                "Help",
                nk_rect(x, y, guiWidth, guiHeight, rect),
                NK_WINDOW_BORDER | NK_WINDOW_TITLE
            )) {
            	
                nk_layout_row_dynamic(ctx, 20, 3);	
                nk_text(ctx, "W,A,S,D,Alt,Space: Camera Movement",  NK_LEFT);
                nk_text(ctx, "Left Mouse: Turn Camera", 			NK_LEFT);
                nk_text(ctx, "Right Mouse: Select/Deselect", 		NK_LEFT);
                
                nk_text(ctx, "Left Mouse + Shift: Rotate Object", 	NK_LEFT);
                nk_text(ctx, "Left Mouse + Ctrl: Translate Object",	NK_LEFT);
                nk_text(ctx, "V: Toggle V-Sync", 					NK_LEFT);
                
                nk_text(ctx, "F: Toggle Fullscreen", 				NK_LEFT);
                nk_text(ctx, "G: Toggle Show GUI", 					NK_LEFT);
                nk_text(ctx, "Esc: Exit", 							NK_LEFT);
              
                nk_end(ctx);
            }
        }
    }    
    

}