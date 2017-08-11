package com.cmp.res.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cmp.res.entity.Apply;
import com.cmp.res.service.ApplyService;


/**
 * 定时检查权限申请是否过期
 * @author trs
 *
 */
@Component
@Lazy(value=false)
public class ApplyTast {
	@Autowired
	private ApplyService applyService;
	
	 @Scheduled(cron = "0 0 1 * * ?") //每天凌晨1点执行
		public void updateMasPic() throws ParseException{
			 System.out.println("权限申请检查任务执行");
			 SimpleDateFormat sdf=new SimpleDateFormat("yyy-MM-dd");
			 long time=sdf.parse(sdf.format(new Date())).getTime();
			 //检查审核中的申请
			List<Apply> applys0=applyService.list(null, 0);
			List<Apply> applys1=applyService.list(null, 1);
			applys0.addAll(applys1);
			for(Apply apply:applys0){
				if(apply.getValidDate().getTime()<time){
					apply.setStatus(3);
					applyService.save(apply);
				}
			}
			 

		}
	

}
