package dev.chiorji.util;

import com.auth0.jwt.interfaces.*;
import java.util.*;
import org.springframework.stereotype.*;

@Service
public class Constant {

	public Constant() {
	}

	public RoleInfo getRole(Map<String, Claim> claims) {
		String email = claims.get("email").asString();
		String role = claims.get("role").asString();
		Integer id = claims.get("id").asInt();
		return new RoleInfo(email, role, id);
	}

	public Boolean sameAuthorIdAndClaimId(Integer claimId, Integer authorId) {
		return Objects.equals(claimId, authorId);
	}
}
