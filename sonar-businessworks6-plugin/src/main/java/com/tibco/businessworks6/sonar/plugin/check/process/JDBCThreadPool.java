package com.tibco.businessworks6.sonar.plugin.check.process;

import java.util.Iterator;
import java.util.List;

import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.tibco.businessworks6.sonar.plugin.check.AbstractProcessCheck;
import com.tibco.businessworks6.sonar.plugin.profile.ProcessSonarWayProfile;
import com.tibco.businessworks6.sonar.plugin.source.ProcessSource;
import com.tibco.businessworks6.sonar.plugin.violation.DefaultViolation;
import com.tibco.businessworks6.sonar.plugin.violation.Violation;
import com.tibco.utils.bw.model.Activity;


@Rule(key = JDBCThreadPool.RULE_KEY, name="JDBC ThreadPool Check", priority = Priority.MAJOR, description = "This rule checks the usage of a Thread Pool on the JDBC Resource")
@BelongsToProfile(title = ProcessSonarWayProfile.defaultProfileName, priority = Priority.MAJOR)
public class JDBCThreadPool extends AbstractProcessCheck {
	public static final String RULE_KEY = "JDBCThreadPool";

	@Override
	protected void validate(ProcessSource processSource) {
		List<Activity> list = processSource.getProcessModel().getActivities();
		for (Iterator<Activity> iterator = list.iterator(); iterator.hasNext();) {
			Activity activity = iterator.next();
			if(activity.getType() != null && activity.getType().contains("bw.jdbc.")){
				if(activity.isJdbcThreadPool()){
					Violation violation = new DefaultViolation(getRule(),
							1,
							"The JDBC activity "+activity.getName()+" is not using a dedicated ThreadPool Shared Resource. Please assign a dedicated ThreadPool SharedResource to the JDBC Activity.");
					processSource.addViolation(violation);
				}
			}
		}
	}
}
