/*
 *    Copyright 2016 Fineo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package io.fineo.schemarepo.dynamo;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class AwsCredentialResource extends ExternalResource {
  private static final Logger LOG = LoggerFactory.getLogger(AwsCredentialResource.class);
  private String CREDENTIALS = "credentials";
  private static final String FAKE_KEY = "AKIAIZFKPYAKBFDZPAEA";
  private static final String FAKE_SECRET = "18S1bF4bpjCKZP2KRgbqOn7xJLDmqmwSXqq5GAWq";
  private AWSCredentialsProvider provider;

  public AwsCredentialResource() {
  }

  public AWSCredentialsProvider getProvider() {
    if (this.provider == null) {
      this.provider = new AWSCredentialsProviderChain(
        new AWSCredentialsProvider[]{this.getSpecifiedFileCredentials(),
          new ProfileCredentialsProvider("aws-testing")});
    }

    return this.provider;
  }

  private AWSCredentialsProvider getSpecifiedFileCredentials() {
    String credentialsFile = System.getProperty(this.CREDENTIALS);
    String key = null;
    String secret = null;
    if (credentialsFile != null) {
      try {
        FileInputStream e = new FileInputStream(credentialsFile);
        Yaml yaml = new Yaml();
        Map map = (Map) yaml.load(e);
        key = (String) map.get("access_key_id");
        secret = (String) map.get("secrect_access_key");
      } catch (FileNotFoundException var7) {
        LOG.warn(
          "Invalid credentials file: " + credentialsFile + "! Skipping test. Specify a " +
          "credentials file with: -D" + this.CREDENTIALS);
      }
    } else {
      LOG.warn("Not credentials file set! Specify a credentials file with: -D" + this.CREDENTIALS);
    }

    return new AWSStaticCredentialsProvider(new AwsCredentialResource.EmptyAllowedCredentials(key,
      secret));
  }

  public AWSCredentialsProvider getFakeProvider() {
    return new AWSStaticCredentialsProvider(new BasicAWSCredentials(FAKE_KEY, FAKE_SECRET));
  }

  private class EmptyAllowedCredentials implements AWSCredentials {
    private final String key;
    private final String secret;

    public EmptyAllowedCredentials(String key, String secret) {
      this.key = key;
      this.secret = secret;
    }

    public String getAWSAccessKeyId() {
      return this.key;
    }

    public String getAWSSecretKey() {
      return this.secret;
    }
  }
}
