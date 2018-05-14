package com.deepoove.poi.tl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.tl.mypolicy.ListDataRenderPolicy;
import com.deepoove.poi.util.BytePictureUtils;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片模板
 * @author Sayi
 * @version 1.0.0
 */
public class PictureRenderTest {

	BufferedImage bufferImage;

	@Before
	public void init(){
		bufferImage = BytePictureUtils.newBufferImage(100, 100);
		Graphics2D g = (Graphics2D)bufferImage.getGraphics();
		g.setColor(Color.red);
		g.fillRect(0, 0, 100, 100);
		g.dispose();
		bufferImage.flush();
	}

	@SuppressWarnings("serial")
	@Test
	public void testPictureRender() throws Exception {
		Map<String, Object> datas = new HashMap<String, Object>() {
			List<PictureRenderData> picList = new ArrayList<PictureRenderData>(){{
				add(new PictureRenderData(100, 120, "src/test/resources/logo.png"));
				add(new PictureRenderData(100, 120, ".png", BytePictureUtils.getLocalByteArray(new File("src/test/resources/logo.png"))));
				add(new PictureRenderData(100, 120, ".png", BytePictureUtils.getBufferByteArray(bufferImage)));
			}};
			{
				//本地图片
				put("localPicture", picList.get(0));
				//本地图片byte数据
				put("localBytePicture", picList.get(1));
				//网路图片
				put("urlPicture", new PictureRenderData(100, 100, ".png", BytePictureUtils.getUrlByteArray("https://avatars3.githubusercontent.com/u/1394854?v=3&s=40")));
				// java 图片
				put("bufferImagePicture", picList.get(2));
				put("pictureList", picList);
			}
		};


		XWPFTemplate template = XWPFTemplate.compile("src/test/resources/picture.docx");
		template.registerPolicy("pictureList", new ListDataRenderPolicy());
		template.render(datas);

		FileOutputStream out = new FileOutputStream("out_picture.docx");
		template.write(out);
		out.flush();
		out.close();
		template.close();
	}


}
