/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.elasticjob.cloud.executor;

import io.elasticjob.cloud.exception.JobConfigurationException;
import io.elasticjob.cloud.executor.type.DataflowJobExecutor;
import io.elasticjob.cloud.fixture.config.TestDataflowJobConfiguration;
import io.elasticjob.cloud.fixture.config.TestScriptJobConfiguration;
import io.elasticjob.cloud.fixture.config.TestSimpleJobConfiguration;
import io.elasticjob.cloud.fixture.job.TestDataflowJob;
import io.elasticjob.cloud.executor.type.ScriptJobExecutor;
import io.elasticjob.cloud.executor.type.SimpleJobExecutor;
import io.elasticjob.cloud.fixture.handler.IgnoreJobExceptionHandler;
import io.elasticjob.cloud.fixture.job.OtherJob;
import io.elasticjob.cloud.fixture.job.TestSimpleJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class JobExecutorFactoryTest {
    
    @Mock
    private JobFacade jobFacade;
    
    @Test
    public void assertGetJobExecutorForScriptJob() {
        when(jobFacade.loadJobRootConfiguration(true)).thenReturn(new TestScriptJobConfiguration("test.sh", IgnoreJobExceptionHandler.class));
        assertThat(JobExecutorFactory.getJobExecutor(null, jobFacade), instanceOf(ScriptJobExecutor.class));
    }
    
    @Test
    public void assertGetJobExecutorForSimpleJob() {
        when(jobFacade.loadJobRootConfiguration(true)).thenReturn(new TestSimpleJobConfiguration());
        assertThat(JobExecutorFactory.getJobExecutor(new TestSimpleJob(null), jobFacade), instanceOf(SimpleJobExecutor.class));
    }
    
    @Test
    public void assertGetJobExecutorForDataflowJob() {
        when(jobFacade.loadJobRootConfiguration(true)).thenReturn(new TestDataflowJobConfiguration(false));
        assertThat(JobExecutorFactory.getJobExecutor(new TestDataflowJob(null), jobFacade), instanceOf(DataflowJobExecutor.class));
    }
    
    @Test(expected = JobConfigurationException.class)
    public void assertGetJobExecutorWhenJobClassWhenUnsupportedJob() {
        JobExecutorFactory.getJobExecutor(new OtherJob(), jobFacade);
    }
    
    @Test
    public void assertGetJobExecutorTwice() {
        when(jobFacade.loadJobRootConfiguration(true)).thenReturn(new TestDataflowJobConfiguration(false));
        AbstractElasticJobExecutor executor = JobExecutorFactory.getJobExecutor(new TestSimpleJob(null), jobFacade);
        AbstractElasticJobExecutor anotherExecutor = JobExecutorFactory.getJobExecutor(new TestSimpleJob(null), jobFacade);
        assertTrue(executor.hashCode() != anotherExecutor.hashCode());
    }
}
