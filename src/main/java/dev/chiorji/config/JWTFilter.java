package dev.chiorji.config;import com.auth0.jwt.*;import com.auth0.jwt.algorithms.*;import com.auth0.jwt.exceptions.*;import com.auth0.jwt.interfaces.*;import jakarta.servlet.*;import jakarta.servlet.http.*;import java.io.*;import org.springframework.http.*;import org.springframework.web.filter.*;public class JWTFilter extends GenericFilterBean {	private final String secretKey;	private final String tokenIssuer;	public JWTFilter(String secretKey, String tokenIssuer) {		this.secretKey = secretKey;		this.tokenIssuer = tokenIssuer;	}	@Override	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {		HttpServletRequest httpServletRequest = (HttpServletRequest) request;		HttpServletResponse httpServletResponse = (HttpServletResponse) response;		String token = this.getBearerTokenFromRequest((HttpServletRequest) request);		if (token == null) {			httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "MISSING AUTHORIZATION TOKEN");			return;		}		try {			DecodedJWT decodedJWT = this.validateJWTToken(token);			Claim id = decodedJWT.getClaim("id");			Claim username = decodedJWT.getClaim("username");			Claim email = decodedJWT.getClaim("email");			Claim role = decodedJWT.getClaim("role");			httpServletRequest.setAttribute("id", id);			httpServletRequest.setAttribute("email", email);			httpServletRequest.setAttribute("username", username);			httpServletRequest.setAttribute("role", role);		} catch (RuntimeException e) {			logger.error("JWT Decoding Error: " + e.getMessage());			httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "INVALID OR EXPIRED AUTHORIZATION TOKEN");			return;		}		chain.doFilter(httpServletRequest, httpServletResponse);	}	private String getBearerTokenFromRequest(HttpServletRequest request) {		String header = request.getHeader("Authorization");		if (header == null) return null;		return header.replace("Bearer ", "");	}	private DecodedJWT validateJWTToken(String jwtToken) {		try {			Algorithm algorithm = Algorithm.HMAC256(secretKey);			return JWT.require(algorithm)				.withIssuer(tokenIssuer)				.build()				.verify(jwtToken);		} catch (JWTVerificationException e) {			throw new RuntimeException(e);		}	}}