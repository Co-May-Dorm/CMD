package com.comaymanagement.cmd.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.constant.Message;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.Post;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.model.DepartmentModel;
import com.comaymanagement.cmd.repository.IPostRepository;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class PostService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IPostRepository postRepositoryImpl;
	@Autowired
	Message message;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
	
	public ResponseEntity<Object> findAll(String title, String content, String sort, String order) {
		title = (title != null && !title.equals(""))? title : "";
		content = (content != null && !content.equals("")) ? content : "";
		List<Post> posts = new ArrayList<>();
		// Order by defaut
		if (sort == null || sort.equals("")) {
			sort = "post.createDate";
		}
		if (order == null || order.equals("")) {
			order = "desc";
		}
		posts = postRepositoryImpl.findAll(title, content,sort, order);
		if (posts.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "", posts));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", "Not found", posts));
		}
	}
	public ResponseEntity<Object> add(String json) {
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectPost;
		try {
			UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			jsonObjectPost = jsonMapper.readTree(json);
			String title = jsonObjectPost.get("title") != null ? jsonObjectPost.get("title").asText() : "";
			String content = jsonObjectPost.get("content") != null ? jsonObjectPost.get("content").asText() : "";
			Boolean isPulished = jsonObjectPost.get("isPulished") != null ? jsonObjectPost.get("isPulished").asBoolean() :true;
			Integer createBy = userDetail.getId();
			Integer modifyBy = userDetail.getId();
			String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime());
			String modifyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime());
			Post post = new Post();
			post.setTitle(title);
			post.setContent(content);
			post.setPulished(isPulished);
			post.setCreateBy(createBy);
			post.setCreateDate(createDate);
			post.setModifyBy(modifyBy);
			post.setModifyDate(modifyDate);
			Integer status =  postRepositoryImpl.add(post);
			if (status != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK",message.getMessageByItemCode("POSTS1") , post));
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR",message.getMessageByItemCode("POSTE1"), post));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured at add() ", e);
			LOGGER.error(json);
			return null;
		}
	}
	public ResponseEntity<Object> edit(String json) {
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectPost;
		try {
			UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			jsonObjectPost = jsonMapper.readTree(json);
//			String id = jsonObjectPost.get("id") != null ? jsonObjectPost.get("id").asText() : "";
			String id = "1";
			String title = jsonObjectPost.get("title") != null ? jsonObjectPost.get("title").asText() : "";
//			String content = jsonObjectPost.get("content") != null ? jsonObjectPost.get("content").asText() : "";
			String content="<html dir=\"ltr\" lang=\"en\"><head><title data-cke-title=\"Rich Text Editor, Post[content]\">Rich Text Editor, Post[content]</title><style data-cke-temp=\"1\">html{cursor:text;*cursor:auto}\r\n"
					+ "    img,input,textarea{cursor:default}</style><link type=\"text/css\" rel=\"stylesheet\" href=\"https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/contents.css?t=J8Q8\"><link type=\"text/css\" rel=\"stylesheet\" href=\"https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/copyformatting/styles/copyformatting.css\"><link type=\"text/css\" rel=\"stylesheet\" href=\"https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/tableselection/styles/tableselection.css\"><link type=\"text/css\" rel=\"stylesheet\" href=\"https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/videodetector//videodetector.css\"><style data-cke-temp=\"1\">.cke_editable{cursor:text}.cke_editable img,.cke_editable input,.cke_editable textarea{cursor:default}\r\n"
					+ "    img.cke_flash{background-image: url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/flash/images/placeholder.png?t=J8Q8);background-position: center center;background-repeat: no-repeat;border: 1px solid #a9a9a9;width: 80px;height: 80px;}\r\n"
					+ "    .cke_editable form{border: 1px dotted #FF0000;padding: 2px;}\r\n"
					+ "    \r\n"
					+ "    img.cke_hidden{background-image: url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/forms/images/hiddenfield.gif?t=J8Q8);background-position: center center;background-repeat: no-repeat;border: 1px solid #a9a9a9;width: 16px !important;height: 16px !important;}\r\n"
					+ "    img.cke_iframe{background-image: url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/iframe/images/placeholder.png?t=J8Q8);background-position: center center;background-repeat: no-repeat;border: 1px solid #a9a9a9;width: 80px;height: 80px;}\r\n"
					+ "    .cke_contents_ltr a.cke_anchor,.cke_contents_ltr a.cke_anchor_empty,.cke_editable.cke_contents_ltr a[name],.cke_editable.cke_contents_ltr a[data-cke-saved-name]{background:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/link/images/anchor.png?t=J8Q8) no-repeat left center;border:1px dotted #00f;background-size:16px;padding-left:18px;cursor:auto;}.cke_contents_ltr img.cke_anchor{background:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/link/images/anchor.png?t=J8Q8) no-repeat left center;border:1px dotted #00f;background-size:16px;width:16px;min-height:15px;height:1.15em;vertical-align:text-bottom;}.cke_contents_rtl a.cke_anchor,.cke_contents_rtl a.cke_anchor_empty,.cke_editable.cke_contents_rtl a[name],.cke_editable.cke_contents_rtl a[data-cke-saved-name]{background:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/link/images/anchor.png?t=J8Q8) no-repeat right center;border:1px dotted #00f;background-size:16px;padding-right:18px;cursor:auto;}.cke_contents_rtl img.cke_anchor{background:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/link/images/anchor.png?t=J8Q8) no-repeat right center;border:1px dotted #00f;background-size:16px;width:16px;min-height:15px;height:1.15em;vertical-align:text-bottom;}\r\n"
					+ "    div.cke_pagebreak{background:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/pagebreak/images/pagebreak.gif?t=J8Q8) no-repeat center center !important;clear:both !important;width:100% !important;border-top:#999 1px dotted !important;border-bottom:#999 1px dotted !important;padding:0 !important;height:7px !important;cursor:default !important;}\r\n"
					+ "    .cke_show_blocks h6:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks h5:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks h4:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks h3:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks h2:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks h1:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks blockquote:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks address:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks pre:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks div:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks p:not([contenteditable=false]):not(.cke_show_blocks_off){background-repeat:no-repeat;border:1px dotted gray;padding-top:8px}.cke_show_blocks h6:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_h6.png?t=J8Q8)}.cke_show_blocks h5:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_h5.png?t=J8Q8)}.cke_show_blocks h4:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_h4.png?t=J8Q8)}.cke_show_blocks h3:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_h3.png?t=J8Q8)}.cke_show_blocks h2:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_h2.png?t=J8Q8)}.cke_show_blocks h1:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_h1.png?t=J8Q8)}.cke_show_blocks blockquote:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_blockquote.png?t=J8Q8)}.cke_show_blocks address:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_address.png?t=J8Q8)}.cke_show_blocks pre:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_pre.png?t=J8Q8)}.cke_show_blocks div:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_div.png?t=J8Q8)}.cke_show_blocks p:not([contenteditable=false]):not(.cke_show_blocks_off){background-image:url(https://canhbaosom.com/plugins/comay/cmrice/formwidgets/wysiwyg/assets/ckeditor/plugins/showblocks/images/block_p.png?t=J8Q8)}.cke_show_blocks.cke_contents_ltr h6:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr h5:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr h4:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr h3:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr h2:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr h1:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr blockquote:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr address:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr pre:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr div:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_ltr p:not([contenteditable=false]):not(.cke_show_blocks_off){background-position:top left;padding-left:8px}.cke_show_blocks.cke_contents_rtl h6:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl h5:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl h4:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl h3:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl h2:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl h1:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl blockquote:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl address:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl pre:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl div:not([contenteditable=false]):not(.cke_show_blocks_off),.cke_show_blocks.cke_contents_rtl p:not([contenteditable=false]):not(.cke_show_blocks_off){background-position:top right;padding-right:8px}\r\n"
					+ "    .cke_show_borders  table.cke_show_border,.cke_show_borders  table.cke_show_border > tr > td, .cke_show_borders  table.cke_show_border > tr > th,.cke_show_borders  table.cke_show_border > tbody > tr > td, .cke_show_borders  table.cke_show_border > tbody > tr > th,.cke_show_borders  table.cke_show_border > thead > tr > td, .cke_show_borders  table.cke_show_border > thead > tr > th,.cke_show_borders  table.cke_show_border > tfoot > tr > td, .cke_show_borders  table.cke_show_border > tfoot > tr > th{border : #d3d3d3 1px dotted}\r\n"
					+ "    .cke_widget_wrapper{position:relative;outline:none}.cke_widget_inline{display:inline-block}.cke_widget_wrapper:hover>.cke_widget_element{outline:2px solid #ffd25c;cursor:default}.cke_widget_wrapper:hover .cke_widget_editable{outline:2px solid #ffd25c}.cke_widget_wrapper.cke_widget_focused>.cke_widget_element,.cke_widget_wrapper .cke_widget_editable.cke_widget_editable_focused{outline:2px solid #47a4f5}.cke_widget_editable{cursor:text}.cke_widget_drag_handler_container{position:absolute;width:15px;height:0;display:block;opacity:0.75;transition:height 0s 0.2s;line-height:0}.cke_widget_wrapper:hover>.cke_widget_drag_handler_container{height:15px;transition:none}.cke_widget_drag_handler_container:hover{opacity:1}.cke_editable[contenteditable=\"false\"] .cke_widget_drag_handler_container{display:none;}img.cke_widget_drag_handler{cursor:move;width:15px;height:15px;display:inline-block}.cke_widget_mask{position:absolute;top:0;left:0;width:100%;height:100%;display:block}.cke_widget_partial_mask{position:absolute;display:block}.cke_editable.cke_widget_dragging, .cke_editable.cke_widget_dragging *{cursor:move !important}\r\n"
					+ "    .cke_upload_uploading img{opacity: 0.3}</style></head><body data-new-gr-c-s-check-loaded=\"14.1068.0\" data-gr-ext-installed=\"\" contenteditable=\"true\" class=\"cke_editable cke_editable_themed cke_contents_ltr cke_show_borders\" spellcheck=\"false\"><h1><br></h1><p style=\"text-align:justify\">Sáng 19/8/2018, Ban tuyển chọn tân sinh viên vào ký túc xá Cỏ May năm học 2018 - 2019 tiến hành họp xét chọn sinh viên khóa 3 tại văn phòng Ban Quản lý ký túc xá (khu phố 6, phường Linh Trung, quận Thủ Đức, TP. Hồ Chí Minh). Năm nay Ký túc xá tiếp tục chính sách “Tiếp bước sinh viên nghèo – học giỏi” trên địa bàn thành phố.</p><p style=\"text-align:justify\">Ký túc xá Cỏ May được đưa vào sử dụng lần đầu tiên từ năm học 2016 – 2017 với gần 200 sinh viên. Năm học 2018 – 2019 là năm thứ ba ký túc xá Cỏ May giúp đỡ các bạn tân sinh viên có hoàn cảnh khó khăn và học giỏi có điều kiện thuận lợi đến trường đại học. Hầu hết sinh viên ở ký túc xá đều có hoàn cảnh “éo le”, có bạn mồ côi cha, mẹ; có bạn bị tật, sức khỏe không tốt. Tuy nhiên các bạn đều có một tinh thần hiếu học.</p><p><img alt=\"\" data-type=\"image\" id=\"innercomp_txtMedia1bkrimgimage\" itemprop=\"image\" data-cke-saved-src=\"https://static.wixstatic.com/media/6a0367_22623861d3384f4a8e36cce59007226c~mv2.jpg/v1/fill/w_630,h_354,al_c,q_80,usm_0.66_1.00_0.01/6a0367_22623861d3384f4a8e36cce59007226c~mv2.webp\" src=\"https://static.wixstatic.com/media/6a0367_22623861d3384f4a8e36cce59007226c~mv2.jpg/v1/fill/w_630,h_354,al_c,q_80,usm_0.66_1.00_0.01/6a0367_22623861d3384f4a8e36cce59007226c~mv2.webp\"></p><p style=\"text-align:justify\">Ban tuyển chọn tân sinh viên năm nay có Bà Nguyễn Ngọc Oanh – Vợ ông Phạm Văn Bên, Chủ tịch HĐTV Công ty TNHH Cỏ May Lai Vung là trưởng ban tuyển chọn, các thành viên còn lại là Thầy Phạm Văn Hiền – Giảng viên Trường Đại học Nông Lâm TP.HCM, Thầy Nguyễn Đức Nghĩa – Phó Chủ tịch Hiệp hội các Trường Đại học Việt Nam, Nguyên Phó giám đốc Đại học Quốc gia, Thầy Nguyễn Ngọc Tuân – Nguyên Giảng viên Trường Đại học Nông Lâm TP.HCM, Cô Trần Thị Dân – Nguyên Trưởng Khoa Thú y, Trường Đại học Nông Lâm TP.HCM; Bà Vũ Kim Hạnh – Nhà báo, Chủ tịch Hội Doanh nghiệp Hàng Việt Nam chất lượng cao và Cô Nguyễn Thị Nhung – Trưởng Ban Quản lý KTX Cỏ May. Trong ngày, ban tuyển chọn sẽ xem tất cả hồ sơ sinh viên nộp và xét chọn sinh viên đúng theo tiêu chí đặt ra.</p><p style=\"text-align:justify\">Năm nay, ký túc xá Cỏ May đã nhận được gần 130 hồ sơ xét tuyển vào ký túc xá của các bạn tân sinh viên nghèo, hiếu học trên các tỉnh thành. Từ ngày 21/8– 23/8/2018 , ban tuyển chọn sẽ phỏng vấn sinh viên được xét chọn để biết thêm về gia cảnh của sinh viên và hướng dẫn sinh viên các thủ tục tiếp theo trước khi vào ở. Kết quả trúng tuyển vào ký túc xá Cỏ May sẽ chính thức được công bố vào ngày 28/8/2018 tại website của Ký túc xá (www.ktxcomay.com.vn). Cô Nguyễn Thị Nhung – Trưởng Ban quản lý ký túc xá cho biết: “hội đồng tuyển chọn sẽ phỏng vấn từng sinh viên trên cơ sở danh sách được xét chọn hôm nay. Tất cả hồ sơ được chọn đều phải đạt từ 4 – 5/5 thành viên chọn”.</p><p><img alt=\"\" data-type=\"image\" id=\"innercomp_txtMedia1xuuimgimage\" itemprop=\"image\" data-cke-saved-src=\"https://static.wixstatic.com/media/6a0367_cb440aa0b45a49beb3f0d9fcf4905aa7~mv2.jpg/v1/fill/w_630,h_420,al_c,q_80,usm_0.66_1.00_0.01/6a0367_cb440aa0b45a49beb3f0d9fcf4905aa7~mv2.webp\" src=\"https://static.wixstatic.com/media/6a0367_cb440aa0b45a49beb3f0d9fcf4905aa7~mv2.jpg/v1/fill/w_630,h_420,al_c,q_80,usm_0.66_1.00_0.01/6a0367_cb440aa0b45a49beb3f0d9fcf4905aa7~mv2.webp\"></p><p style=\"text-align:justify\">Cô Vũ Kim Hạnh cũng tâm sự về tiêu chí xét tuyển năm nay: “Tôi quan tâm nhất là hoàn cảnh gia đình, học lực, thành tích điểm thi Trung học phổ thông Quốc gia của các sinh viên; đặc biệt là ưu tiên các bạn hiếu học nhưng hoàn cảnh quá khó khăn để tạo cơ hội cho các bạn được ở ký túc xá Cỏ May và được tiếp sức vào giảng đường Đại học.”</p><p style=\"text-align:justify\">Ký túc xá Cỏ May, một nơi ở với nhiều tiện nghi, giúp cho sinh viên bớt đi những lo âu trong những ngày trở thành sinh viên trên giảng đường đại học. Xin cám ơn người sáng lập ký túc xá này, doanh nhân tử tế Phạm Văn Bên và công ty TNHH Cỏ May Lai Vung</p><p style=\"text-align:justify\">Tác giả: Kim Phượng - Minh Tuấn</p></body><grammarly-desktop-integration data-grammarly-shadow-root=\"true\"></grammarly-desktop-integration></html>";
			Boolean isPulished = jsonObjectPost.get("isPulished") != null ? jsonObjectPost.get("isPulished").asBoolean() :true;
			Integer modifyBy = userDetail.getId();
			String modifyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime());
			Post post = postRepositoryImpl.findById(Integer.valueOf(id));
			post.setTitle(title);
			post.setContent(content);
			post.setPulished(isPulished);
			post.setModifyBy(modifyBy);
			post.setModifyDate(modifyDate);
			Integer status =  postRepositoryImpl.edit(post);
			if (status != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK",message.getMessageByItemCode("POSTS2") , post));
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR",message.getMessageByItemCode("POSTE2"), post));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured at edit() ", e);
			LOGGER.error(json);
			return null;
		}
	}
	public ResponseEntity<Object> delete(Integer id){
		Post postDelete = (Post)  postRepositoryImpl.findById(id);
		String deleteStatus = postRepositoryImpl.delete(id);
		try {
			if (deleteStatus.equals("1")) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message.getMessageByItemCode("POSTS3"), ""));
		} else {
				return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ERROR", message.getMessageByItemCode("POSTS3"), ""));

			}
		} catch (Exception e) {
			LOGGER.error("Has error: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("ERROR", e.getMessage(), ""));
			}
		}
	
}
