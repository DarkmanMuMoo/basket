package com.shop.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ValidationClientService {
	@GET("validate")
	Call<ValidateResponse> validateVat(@Query("vat_number") String sort, @Query("access_key") String key);

}
