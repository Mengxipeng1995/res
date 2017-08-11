package com.cmp.res.util;

/**
 * 表示HTML页面中置标某一个参数的对象。
 * 在进行连接替换或WCM图片URL替换中使用到。
 *
 */
public class ParameterfObject
{
	String m_ParameterName;	//参数名
	String m_ParameterValue;	//参数值
	char m_Mark;	//分隔符
	int m_Begin,m_End;	//置标参数开始及结束位置
	
	/**
 	* ParameterfObject 构造子注解。
 	*/
	public ParameterfObject()
    {
    	super();
    	m_ParameterName="";
    	m_ParameterValue="";
    	m_Mark='"';
    	m_Begin = 0;
    	m_End = 0;
    }

    /**
     * 设置参数名
     *
     * @param value String 参数名
     *
     */
    public void setParameterName(String value)
    {
    	m_ParameterName = value;
    }
    
    /**
     *
     * 获得参数名
     *
     * @return <code>String</code> 参数名
     *
     */
    public String getParameterName()
    {
    	return m_ParameterName;
    }

    /**
     * 设置参数值
     *
     * @param value String 参数值
     *
     */
    public void setParameterValue(String value)
    {
    	m_ParameterValue = value;
    }
    
    /**
     *
     * 获得参数值
     *
     * @return <code>String</code> 参数值
     *
     */
    public String getParameterValue()
    {
    	return m_ParameterValue;
    }

    /**
     * 设置分隔符
     *
     * @param value char 分隔符
     *
     */
    public void setMark(char value)
    {
    	m_Mark = value;
    }
    
    /**
     *
     * 获得分隔符
     *
     * @return <code>char</code> 分隔符
     *
     */
    public char getMark()
    {
    	return m_Mark;
    }

    /**
     * 设置参数开始位置
     *
     * @param value int 参数开始位置
     *
     */
    public void setBegin(int value)
    {
    	m_Begin = value;
    }
    
    /**
     *
     * 获得参数开始位置
     *
     * @return <code>int</code> 参数开始位置
     *
     */
    public int getBegin()
    {
    	return m_Begin;
    }

    /**
     * 设置参数结束位置
     *
     * @param value int 参数结束位置
     *
     */
    public void setEnd(int value)
    {
    	m_End = value;
    }
    
    /**
     *
     * 获得参数结束位置
     *
     * @return <code>int</code> 参数结束位置
     *
     */
    public int getEnd()
    {
    	return m_End;
    }
}