package cgthk.util;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendEquation;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.nuklear.*;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;


public class NuklearHelper {
	
	private long window;
	
	// GUI Elements
	private static final int BUFFER_INITIAL_SIZE = 4 * 1024;

	public static final int MAX_VERTEX_BUFFER  = 512 * 1024;
    public static final int MAX_ELEMENT_BUFFER = 128 * 1024;

    private static final NkAllocator ALLOCATOR;
	    
	private static final NkDrawVertexLayoutElement.Buffer VERTEX_LAYOUT;

	static {
	    ALLOCATOR = NkAllocator.create();
	    ALLOCATOR.alloc((handle, old, size) -> {
	        long mem = nmemAlloc(size);
	        if (mem == NULL) {
	            throw new OutOfMemoryError();
	        }

	        return mem;
        });
	    ALLOCATOR.mfree((handle, ptr) -> nmemFree(ptr));

	    VERTEX_LAYOUT = NkDrawVertexLayoutElement.create(4)
	        .position(0).attribute(NK_VERTEX_POSITION).format(NK_FORMAT_FLOAT).offset(0)
	        .position(1).attribute(NK_VERTEX_TEXCOORD).format(NK_FORMAT_FLOAT).offset(8)
	        .position(2).attribute(NK_VERTEX_COLOR).format(NK_FORMAT_R8G8B8A8).offset(16)
	        .position(3).attribute(NK_VERTEX_ATTRIBUTE_COUNT).format(NK_FORMAT_COUNT).offset(0)
	        .flip();
	    }
		
		private int vbo, vao, ebo;
	    private int prog;
	    private int vert_shdr;
	    private int frag_shdr;
	    private int uniform_tex;
	    private int uniform_proj;
		
	    public NkContext  ctx          = NkContext.create();
	    private NkUserFont default_font = NkUserFont.create();

	    private NkBuffer          cmds         = NkBuffer.create();
	    private NkDrawNullTexture null_texture = NkDrawNullTexture.create();
	    
	    private final ByteBuffer ttf;
	    
	    public NuklearHelper( long window ){
	    	
	    	this.window = window;
	    	
	    	try {
	            this.ttf = IOUtils.ioResourceToByteBuffer("resources"+ File.separator + "Font" + File.separator + "FiraSans.ttf", 512 * 1024);
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    	
	    }
	    
	    
	    public void init(){
	    	NkContext ctx = setupWindowInteraction(window);
	    	
	        int BITMAP_W = 1024;
	        int BITMAP_H = 1024;

	        int FONT_HEIGHT = 18;
	        int fontTexID   = glGenTextures();

	        STBTTFontinfo          fontInfo = STBTTFontinfo.create();
	        STBTTPackedchar.Buffer cdata    = STBTTPackedchar.create(95);

	        float scale;
	        float descent;

	        try (MemoryStack stack = stackPush()) {
	            stbtt_InitFont(fontInfo, ttf);
	            scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_HEIGHT);

	            IntBuffer d = stack.mallocInt(1);
	            stbtt_GetFontVMetrics(fontInfo, null, d, null);
	            descent = d.get(0) * scale;

	            ByteBuffer bitmap = memAlloc(BITMAP_W * BITMAP_H);

	            STBTTPackContext pc = STBTTPackContext.mallocStack(stack);
	            stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
	            stbtt_PackSetOversampling(pc, 4, 4);
	            stbtt_PackFontRange(pc, ttf, 0, FONT_HEIGHT, 32, cdata);
	            stbtt_PackEnd(pc);

	            // Convert R8 to RGBA8
	            ByteBuffer texture = memAlloc(BITMAP_W * BITMAP_H * 4);
	            for (int i = 0; i < bitmap.capacity(); i++) {
	                texture.putInt((bitmap.get(i) << 24) | 0x00FFFFFF);
	            }
	            texture.flip();

	            glBindTexture(GL_TEXTURE_2D, fontTexID);
	            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, BITMAP_W, BITMAP_H, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, texture);
	            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

	            memFree(texture);
	            memFree(bitmap);
	        }

	        default_font
	            .width((handle, h, text, len) -> {
	                float text_width = 0;
	                try (MemoryStack stack = stackPush()) {
	                    IntBuffer unicode = stack.mallocInt(1);

	                    int glyph_len = nnk_utf_decode(text, memAddress(unicode), len);
	                    int text_len  = glyph_len;

	                    if (glyph_len == 0) {
	                        return 0;
	                    }

	                    IntBuffer advance = stack.mallocInt(1);
	                    while (text_len <= len && glyph_len != 0) {
	                        if (unicode.get(0) == NK_UTF_INVALID) {
	                            break;
	                        }

	                        /* query currently drawn glyph information */
	                        stbtt_GetCodepointHMetrics(fontInfo, unicode.get(0), advance, null);
	                        text_width += advance.get(0) * scale;

							/* offset next glyph */
	                        glyph_len = nnk_utf_decode(text + text_len, memAddress(unicode), len - text_len);
	                        text_len += glyph_len;
	                    }
	                }
	                return text_width;
	            })
	            .height(FONT_HEIGHT)
	            .query((handle, font_height, glyph, codepoint, next_codepoint) -> {
	                try (MemoryStack stack = stackPush()) {
	                    FloatBuffer x = stack.floats(0.0f);
	                    FloatBuffer y = stack.floats(0.0f);

	                    STBTTAlignedQuad q       = STBTTAlignedQuad.mallocStack(stack);
	                    IntBuffer        advance = stack.mallocInt(1);

	                    stbtt_GetPackedQuad(cdata, BITMAP_W, BITMAP_H, codepoint - 32, x, y, q, false);
	                    stbtt_GetCodepointHMetrics(fontInfo, codepoint, advance, null);

	                    NkUserFontGlyph ufg = NkUserFontGlyph.create(glyph);

	                    ufg.width(q.x1() - q.x0());
	                    ufg.height(q.y1() - q.y0());
	                    ufg.offset().set(q.x0(), q.y0() + (FONT_HEIGHT + descent));
	                    ufg.xadvance(advance.get(0) * scale);
	                    ufg.uv(0).set(q.s0(), q.t0());
	                    ufg.uv(1).set(q.s1(), q.t1());
	                }
	            })
	            .texture().id(fontTexID);

	        nk_style_set_font(ctx, default_font);
	    }
	    
		private  void setupContext() {
	        String NK_SHADER_VERSION = Platform.get() == Platform.MACOSX ? "#version 150\n" : "#version 300 es\n";
	        String vertex_shader =
	            NK_SHADER_VERSION +
	            "uniform mat4 ProjMtx;\n" +
	            "in vec2 Position;\n" +
	            "in vec2 TexCoord;\n" +
	            "in vec4 Color;\n" +
	            "out vec2 Frag_UV;\n" +
	            "out vec4 Frag_Color;\n" +
	            "void main() {\n" +
	            "   Frag_UV = TexCoord;\n" +
	            "   Frag_Color = Color;\n" +
	            "   gl_Position = ProjMtx * vec4(Position.xy, 0, 1);\n" +
	            "}\n";
	        String fragment_shader =
	            NK_SHADER_VERSION +
	            "precision mediump float;\n" +
	            "uniform sampler2D Texture;\n" +
	            "in vec2 Frag_UV;\n" +
	            "in vec4 Frag_Color;\n" +
	            "out vec4 Out_Color;\n" +
	            "void main(){\n" +
	            "   Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n" +
	            "}\n";

	        nk_buffer_init(cmds, ALLOCATOR, BUFFER_INITIAL_SIZE);
	        prog = glCreateProgram();
	        vert_shdr = glCreateShader(GL_VERTEX_SHADER);
	        frag_shdr = glCreateShader(GL_FRAGMENT_SHADER);
	        glShaderSource(vert_shdr, vertex_shader);
	        glShaderSource(frag_shdr, fragment_shader);
	        glCompileShader(vert_shdr);
	        glCompileShader(frag_shdr);
	        if (glGetShaderi(vert_shdr, GL_COMPILE_STATUS) != GL_TRUE) {
	            throw new IllegalStateException();
	        }
	        if (glGetShaderi(frag_shdr, GL_COMPILE_STATUS) != GL_TRUE) {
	            throw new IllegalStateException();
	        }
	        glAttachShader(prog, vert_shdr);
	        glAttachShader(prog, frag_shdr);
	        glLinkProgram(prog);
	        if (glGetProgrami(prog, GL_LINK_STATUS) != GL_TRUE) {
	            throw new IllegalStateException();
	        }

	        uniform_tex = glGetUniformLocation(prog, "Texture");
	        uniform_proj = glGetUniformLocation(prog, "ProjMtx");
	        int attrib_pos = glGetAttribLocation(prog, "Position");
	        int attrib_uv  = glGetAttribLocation(prog, "TexCoord");
	        int attrib_col = glGetAttribLocation(prog, "Color");

	        {
	            // buffer setup
	            vbo = glGenBuffers();
	            ebo = glGenBuffers();
	            vao = glGenVertexArrays();

	            glBindVertexArray(vao);
	            glBindBuffer(GL_ARRAY_BUFFER, vbo);
	            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

	            glEnableVertexAttribArray(attrib_pos);
	            glEnableVertexAttribArray(attrib_uv);
	            glEnableVertexAttribArray(attrib_col);

	            glVertexAttribPointer(attrib_pos, 2, GL_FLOAT, false, 20, 0);
	            glVertexAttribPointer(attrib_uv, 2, GL_FLOAT, false, 20, 8);
	            glVertexAttribPointer(attrib_col, 4, GL_UNSIGNED_BYTE, true, 20, 16);
	        }

	        {
	            // null texture setup
	            int nullTexID = glGenTextures();

	            null_texture.texture().id(nullTexID);
	            null_texture.uv().set(0.5f, 0.5f);

	            glBindTexture(GL_TEXTURE_2D, nullTexID);
	            try (MemoryStack stack = stackPush()) {
	                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, stack.ints(0xFFFFFFFF));
	            }
	            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	        }

	        glBindTexture(GL_TEXTURE_2D, 0);
	        glBindBuffer(GL_ARRAY_BUFFER, 0);
	        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	        glBindVertexArray(0);
	    }
		
		/**
		 * Called from Main
		 */
		public void pollEvents(){
			nk_input_begin(ctx);
			
			glfwPollEvents();
        
			nk_input_end(ctx);
		}
		
		/**
		 * Gets called from Main
		 *  
		 * @param ctx
		 * @param window
		 * @param button
		 * @param action
		 */
		public void setupMouseInteractions(NkContext ctx, long window, int button, int action){
			try (MemoryStack stack = stackPush()) {
                DoubleBuffer cx = stack.mallocDouble(1);
                DoubleBuffer cy = stack.mallocDouble(1);

                glfwGetCursorPos(window, cx, cy);
                
                int x = (int)cx.get(0);
                int y = (int)cy.get(0);

                int nkButton;
                switch (button) {
                    case GLFW_MOUSE_BUTTON_RIGHT:
                        nkButton = NK_BUTTON_RIGHT;
                        break;
                    case GLFW_MOUSE_BUTTON_MIDDLE:
                        nkButton = NK_BUTTON_MIDDLE;
                        break;
                    default:
                        nkButton = NK_BUTTON_LEFT;
                }
                nk_input_button(ctx, nkButton, x, y, action == GLFW_PRESS);
            }
		}
		
		private NkContext setupWindowInteraction( long win ) {
			
			glfwSetCursorPosCallback(win, (windows, xpos, ypos) -> nk_input_motion(ctx, (int)xpos, (int)ypos));
			/*
	        glfwSetMouseButtonCallback(win, (window, button, action, mods) -> {
	            try (MemoryStack stack = stackPush()) {
	                DoubleBuffer cx = stack.mallocDouble(1);
	                DoubleBuffer cy = stack.mallocDouble(1);
	
	                glfwGetCursorPos(window, cx, cy);
	                
	                int x = (int)cx.get(0);
	                int y = (int)cy.get(0);
	
	                int nkButton;
	                switch (button) {
	                    case GLFW_MOUSE_BUTTON_RIGHT:
	                        nkButton = NK_BUTTON_RIGHT;
	                        break;
	                    case GLFW_MOUSE_BUTTON_MIDDLE:
	                        nkButton = NK_BUTTON_MIDDLE;
	                        break;
	                    default:
	                        nkButton = NK_BUTTON_LEFT;
	                }
	                nk_input_button(ctx, nkButton, x, y, action == GLFW_PRESS);
	            }
	        });*/
	        
	        nk_init(ctx, ALLOCATOR, null);
	        ctx.clip().copy((handle, text, len) -> {
	            if (len == 0) {
	                return;
	            }

	            try (MemoryStack stack = stackPush()) {
	                ByteBuffer str = stack.malloc(len + 1);
	                memCopy(text, memAddress(str), len);
	                str.put(len, (byte)0);

	                glfwSetClipboardString(win, str);
	            }
	        });
	        
	        ctx.clip().paste((handle, edit) -> {
	            long text = nglfwGetClipboardString(win);
	            if (text != NULL) {
	                nnk_textedit_paste(edit, text, nnk_strlen(text));
	            }
	        });
	        
	        setupContext();
	        return ctx;
	    }
		
		public void render(int AA, int max_vertex_buffer, int max_element_buffer, int display_width, int display_height, int width, int height) {
	        OpenGLState currentState = new OpenGLState();
			try (MemoryStack stack = stackPush()) {
	            // setup global state
	            glEnable(GL_BLEND);
	            glBlendEquation(GL_FUNC_ADD);
	            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	            glDisable(GL_CULL_FACE);
	            glDisable(GL_DEPTH_TEST);
	            glEnable(GL_SCISSOR_TEST);
	            glActiveTexture(GL_TEXTURE0);

	            // setup program
	            glUseProgram(prog);
	            glUniform1i(uniform_tex, 0);
	            glUniformMatrix4fv(uniform_proj, false, stack.floats(
	                2.0f / width, 0.0f, 0.0f, 0.0f,
	                0.0f, -2.0f / height, 0.0f, 0.0f,
	                0.0f, 0.0f, -1.0f, 0.0f,
	                -1.0f, 1.0f, 0.0f, 1.0f
	            ));
	            glViewport(0, 0, display_width, display_height);
	        }
			
	        {
	            // convert from command queue into draw list and draw to screen

	            // allocate vertex and element buffer
	            glBindVertexArray(vao);
	            glBindBuffer(GL_ARRAY_BUFFER, vbo);
	            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

	            glBufferData(GL_ARRAY_BUFFER, max_vertex_buffer, GL_STREAM_DRAW);
	            glBufferData(GL_ELEMENT_ARRAY_BUFFER, max_element_buffer, GL_STREAM_DRAW);

	            // load draw vertices & elements directly into vertex + element buffer
	            ByteBuffer vertices = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, max_vertex_buffer, null);
	            ByteBuffer elements = glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY, max_element_buffer, null);
	            try (MemoryStack stack = stackPush()) {
	                // fill convert configuration
	                NkConvertConfig config = NkConvertConfig.callocStack(stack)
	                    .vertex_layout(VERTEX_LAYOUT)
	                    .vertex_size(20)
	                    .vertex_alignment(4)
	                    .null_texture(null_texture)
	                    .circle_segment_count(22)
	                    .curve_segment_count(22)
	                    .arc_segment_count(22)
	                    .global_alpha(1.0f)
	                    .shape_AA(AA)
	                    .line_AA(AA);

	                // setup buffers to load vertices and elements
	                NkBuffer vbuf = NkBuffer.mallocStack(stack);
	                NkBuffer ebuf = NkBuffer.mallocStack(stack);
	                
	                

	                nk_buffer_init_fixed(vbuf, vertices/*, max_vertex_buffer*/);
	                nk_buffer_init_fixed(ebuf, elements/*, max_element_buffer*/);
	                nk_convert(ctx, cmds, vbuf, ebuf, config);
	            }
	            glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
	            glUnmapBuffer(GL_ARRAY_BUFFER);
	            
	            // iterate over and execute each draw command
	            float fb_scale_x = (float)display_width / (float)width;
	            float fb_scale_y = (float)display_height / (float)height;
	            
	            long offset = NULL;
	            for (NkDrawCommand cmd = nk__draw_begin(ctx, cmds); cmd != null; cmd = nk__draw_next(cmd, cmds, ctx)) {
	                if (cmd.elem_count() == 0) {
	                    continue;
	                }
	                
	                glBindTexture(GL_TEXTURE_2D, cmd.texture().id());
	                glScissor(
	                    (int)(cmd.clip_rect().x() * fb_scale_x),
	                    (int)((height - (int)(cmd.clip_rect().y() + cmd.clip_rect().h())) * fb_scale_y),
	                    (int)(cmd.clip_rect().w() * fb_scale_x),
	                    (int)(cmd.clip_rect().h() * fb_scale_y)
	                );
	                glDrawElements(GL_TRIANGLES, cmd.elem_count(), GL_UNSIGNED_SHORT, offset);
	                offset += cmd.elem_count() * 2;
	                
	            }
	            nk_clear(ctx);
	        }
	        currentState.restoreState();	        
	    }
		
		private void destroy() {
	        glDetachShader(prog, vert_shdr);
	        glDetachShader(prog, frag_shdr);
	        glDeleteShader(vert_shdr);
	        glDeleteShader(frag_shdr);
	        glDeleteProgram(prog);
	        glDeleteTextures(default_font.texture().id());
	        glDeleteTextures(null_texture.texture().id());
	        glDeleteBuffers(vbo);
	        glDeleteBuffers(ebo);
	        nk_buffer_free(cmds);
	    }

		public void shutdown() {
	        ctx.clip().copy().free();
	        ctx.clip().paste().free();
	        nk_free(ctx);
	        destroy();
	        default_font.query().free();
	        default_font.width().free();

	        ALLOCATOR.alloc().free();
	        ALLOCATOR.mfree().free();
	    }
}
