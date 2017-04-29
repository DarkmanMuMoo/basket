package com.shop.client;

import static com.shop.exception.APIException.internalError;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.exception.APIException;

import retrofit2.Call;
import retrofit2.Response;

@Service
public class ValidationService {

	private ValidationClientService service;

	private APILayerConfiguration config;

	@Autowired
	public ValidationService(ValidationClientService service, APILayerConfiguration config) {
		this.service = service;
		this.config = config;
	}

	public boolean validateVat(String vat) throws APIException {

		Call<ValidateResponse> call = service.validateVat(vat, config.getKey());

		try {
			Response<ValidateResponse> response = call.execute();
			if (response.isSuccessful()) {
				return response.body().getValid() == null ? false : response.body().getValid();
			}
			throw internalError("got error response from validation service");
		} catch (IOException e) {
			throw internalError("problem when connect to validation service", e);
		}

	}

}
