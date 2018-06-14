package module.manager.ldap;

import org.w3c.dom.Document;

/**
 * 该内部类定义从OpenLDAP数据库取出的一条记录的格式
 * topicName	主题的名字
 * topicCode	主题的编码
 * topicPath	主题在主题树中的路径
 *
 * @author WenPeng
 */
public class TopicEntry implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String topicName = null;
	private String topicCode = null;
	private String topicPath = null;
	private WsnPolicyMsg wsnpolicymsg = null;
	private Document schema = null;
	
	public Document getSchema() {
		return schema;
	}
	public void setSchema(Document schema) {
		this.schema = schema;
	}
	public TopicEntry(){}
	public TopicEntry(String _topicName, String _topicCode, 
			String _topicPath, WsnPolicyMsg _wsnpolicymsg){
		this.topicName = _topicName;
		this.topicCode = _topicCode;
		this.topicPath = _topicPath;
		this.wsnpolicymsg = _wsnpolicymsg;
	}
	
	public TopicEntry(String _topicName, String _topicCode, 
			String _topicPath, WsnPolicyMsg _wsnpolicymsg,Document _schema){
		this.topicName = _topicName;
		this.topicCode = _topicCode;
		this.topicPath = _topicPath;
		this.wsnpolicymsg = _wsnpolicymsg;
		this.schema = _schema;
	}
	
	
	public String getTopicPath() {
		return topicPath;
	}
	public void setTopicPath(String topicPath) {
		this.topicPath = topicPath;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getTopicCode() {
		return topicCode;
	}
	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}
	public WsnPolicyMsg getWsnpolicymsg() {
		return wsnpolicymsg;
	}
	public void setWsnpolicymsg(WsnPolicyMsg wsnpolicymsg) {
		this.wsnpolicymsg = wsnpolicymsg;
	}
	@Override
	public String toString(){
		return getTopicName();
	}
}
