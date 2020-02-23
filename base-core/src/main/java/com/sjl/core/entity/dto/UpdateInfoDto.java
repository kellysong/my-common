package com.sjl.core.entity.dto;

/**
 * 更新信息dto
 * 
 * @author song
 *
 */
public class UpdateInfoDto {

	/**
	 * hasUpdate : true
	 * versionCode : 1039
	 * versionName : 3.6.3
	 * updateContent : 1. 修复若干升级9.0后出现的若干bug ; 2. 加入异常机制.
	 * url : nullbookmark_V3.6.3_release_2019-12-24.apk
	 * md5 : 3ccc51f7862d8dfa9583c7bc8984b162
	 * size : 12539630
	 * silent : false
	 * force : false
	 * autoInstall : true
	 * ignorable : false
	 */

	private boolean hasUpdate;
	private int versionCode;
	private String versionName;
	private String updateContent;
	private String url;
	private String md5;
	private int size;
	private boolean silent;
	private boolean force;
	private boolean autoInstall;
	private boolean ignorable;

	public boolean isHasUpdate() {
		return hasUpdate;
	}

	public void setHasUpdate(boolean hasUpdate) {
		this.hasUpdate = hasUpdate;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getUpdateContent() {
		return updateContent;
	}

	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	public boolean isAutoInstall() {
		return autoInstall;
	}

	public void setAutoInstall(boolean autoInstall) {
		this.autoInstall = autoInstall;
	}

	public boolean isIgnorable() {
		return ignorable;
	}

	public void setIgnorable(boolean ignorable) {
		this.ignorable = ignorable;
	}

	@Override
	public String toString() {
		return "UpdateInfoDto{" +
				"hasUpdate=" + hasUpdate +
				", versionCode=" + versionCode +
				", versionName='" + versionName + '\'' +
				", updateContent='" + updateContent + '\'' +
				", url='" + url + '\'' +
				", md5='" + md5 + '\'' +
				", size=" + size +
				", silent=" + silent +
				", force=" + force +
				", autoInstall=" + autoInstall +
				", ignorable=" + ignorable +
				'}';
	}
}
