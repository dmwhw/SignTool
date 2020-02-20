package temperatureserver;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import temperatureserver.SignTool.SignException;

public class TestSignTool {
	
	
	Map<String,Object> requestData=new HashMap<>();

	
	@org.junit.Before
	public void before(){
		//-------------------------------初始化apiKey
		Map<String,String> keys=new HashMap<>();
		keys.put("device1","466aeb9da26c43f295fec28972714861");
		keys.put("device2","a3e2dea109d44dffa9332de107f2a5fd");
		SignTool.loadApiKeys(keys);

	}
	
	@Test
	public void testValidate(){
		//--------------------------------------模拟客户端数据
		Map<String, Object> simulateData = getSimulateData();
		System.out.println("-----客户端数据----");
		System.out.println(simulateData);
		//--------------------------------------------------
		//----验证
		Boolean result=null;
		try {
			System.err.println("-----校验结果----");
			result= SignTool.validateSign(simulateData);
			if (result){
				System.out.println("验证通过");
			}
			return ;
		} catch (SignException e) {
			 System.err.println(" validate not success code is "+e.code);
		}
		System.err.println("验证不通过");
	}
	
	
	@Test 
	public void testInvalidate(){
		//--------------------------------------模拟客户端数据
		Map<String, Object> simulateData = getSimulateData();
		System.out.println("-----客户端数据----");
		System.out.println(simulateData);
		System.err.println("-----伪造数据----");
		simulateData.put("id", "440103199303076811");
		//--------------------------------------------------
		//----验证
		Boolean result=null;
		try {
			System.err.println("-----校验结果----");

			result= SignTool.validateSign(simulateData);
			if (result){
				System.out.println("验证通过");
			}
			return ;
		} catch (SignException e) {
			 System.err.println(" validate not success code is "+e.code);
		}
		System.err.println("验证不通过");
	}
	@Test 
	public void testvalidate2(){
		//--------------------------------------模拟客户端数据
		Map<String, Object> simulateData = getSimulateData();
		System.out.println("-----客户端数据----");
		System.out.println(simulateData);
		System.err.println("-----删除机构数据----");
		simulateData.remove("username");
		//--------------------------------------------------
		//----验证
		Boolean result=null;
		try {
			System.err.println("-----校验结果----");

			result= SignTool.validateSign(simulateData);
			if (result){
				System.out.println("验证通过");
			}
			return ;
		} catch (SignException e) {
			 System.err.println(" validate not success code is "+e.code);
		}
		System.err.println("验证不通过");
	}
	@Test 
	public void testvalidate8(){
		//--------------------------------------模拟客户端数据假签名
		Map<String, Object> simulateData = getSimulateData();
		System.out.println("-----客户端数据假签名----");
		System.out.println(simulateData);
		simulateData.put("sign", "222222");
		//--------------------------------------------------
		//----验证
		Boolean result=null;
		try {
			System.err.println("-----校验结果----");

			result= SignTool.validateSign(simulateData);
			if (result){
				System.out.println("验证通过");
			}
			return ;
		} catch (SignException e) {
			 System.err.println(" validate not success code is "+e.code);
		}
		System.err.println("验证不通过");
	}
	@Test 
	public void testvalidate3(){
		//--------------------------------------模拟客户端数据
		Map<String, Object> simulateData = getSimulateData();
		System.out.println("-----客户端数据----");
		System.out.println(simulateData);
		System.err.println("-----删除时间----");
		simulateData.remove("time");
		//--------------------------------------------------
		//----验证
		Boolean result=null;
		try {
			System.err.println("-----校验结果----");

			result= SignTool.validateSign(simulateData);
			if (result){
				System.out.println("验证通过");
			}
			return ;
		} catch (SignException e) {
			 System.err.println(" validate not success code is "+e.code);
		}
		System.err.println("验证不通过");
	}
	@Test 
	public void testvalidate4(){
		//--------------------------------------模拟客户端数据过期
		Map<String, Object> simulateData = new HashMap<>();
		simulateData.put("temperature", 37.2);
		simulateData.put("sign", "5e8419e08338918559a479f37946ddab");
		simulateData.put("id","44010319930307681X");
		simulateData.put("time","1582190075533");
		simulateData.put("username","device1");

		System.out.println("-----客户端数据过期模拟----");
		System.out.println(simulateData);

		//--------------------------------------------------
		//----验证
		Boolean result=null;
		try {
			System.err.println("-----校验结果----");

			result= SignTool.validateSign(simulateData);
			if (result){
				System.out.println("验证通过");
			}
			return ;
		} catch (SignException e) {
			 System.err.println(" validate not success code is "+e.code);
		}
		System.err.println("验证不通过");
	}
	
	@Test 
	public void testvalidate5(){
		//--------------------------------------模拟客户端数据无签名
		Map<String, Object> simulateData = new HashMap<>();
		simulateData.put("temperature", 37.2);
		//simulateData.put("sign", "5e8419e08338918559a479f37946ddab");
		simulateData.put("id","44010319930307681X");
		simulateData.put("time","1582190075533");
		simulateData.put("username","device1");

		System.out.println("-----客户端数据无签名----");
		System.out.println(simulateData);

		//--------------------------------------------------
		//----验证
		Boolean result=null;
		try {
			System.err.println("-----校验结果----");

			result= SignTool.validateSign(simulateData);
			if (result){
				System.out.println("验证通过");
			}
			return ;
		} catch (SignException e) {
			 System.err.println(" validate not success code is "+e.code);
		}
		System.err.println("验证不通过");
	}
	
	private Map<String,Object> getSimulateData(){
		//-------------------------------模拟客户端数据---------------------
		requestData.put("temperature", 37.2);
		requestData.put("id", "44010319930307681X");
		SignTool.putSign(requestData, "device1");
		//-------------------------------模拟客户端数据---------------------
		return requestData;
	}
	
 
}
