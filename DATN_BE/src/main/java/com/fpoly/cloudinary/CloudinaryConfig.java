package com.fpoly.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
	@Bean
	public Cloudinary cloudinary() {
		Map<String, String> config = new HashMap<>();
		config.put("cloud_name", "dxj6jmdm8");
		config.put("api_key", "972237799824192");
		config.put("api_secret", "L-3iKqqgg966eTcMHcTGQDFgln0");
		return new Cloudinary(config);
	}
}
