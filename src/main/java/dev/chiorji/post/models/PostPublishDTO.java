package dev.chiorji.post.models;

import org.springframework.web.multipart.*;

public class PostPublishDTO {
	private String title;

	private String content;

	private String excerpt;

	private MultipartFile poster_card;

	private String tags;

	private String status;

	private Integer author_id;

	private Integer category;

	private Boolean featured;

	public Boolean getFeatured() {
		return featured;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(Integer author_id) {
		this.author_id = author_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public MultipartFile getPoster_card() {
		return poster_card;
	}

	public void setPoster_card(MultipartFile poster_card) {
		this.poster_card = poster_card;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
