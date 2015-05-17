package controllers;

import java.util.Map;
import play.libs.F.Function;

import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.*;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

public class DistanceController extends Controller {

	public static Result sendJsonRequest() {

		final String mapsUrl = "https://maps.googleapis.com/maps/api/distancematrix/json";
		final Map<String, String> params = Maps.newHashMap();
		params.put("sensor", "false");

		final String[] origins = { "43.854546, 18.3780038" };
		params.put("origins", Joiner.on('|').join(origins));
		final String[] destionations = { "43.854546, 18.9780038" };
		params.put("destinations", Joiner.on('|').join(destionations));
		params.put("key", "AIzaSyCSn4UqEXVZ9483KhlyAKVrRBfWc54ihKc");

		final String url = mapsUrl + '?' + Application.encodeParams(params);
        System.out.println(url);
		
        Promise<Result> jsonPromise = WS.url(url).setContentType("application/json").get().map(
		        new Function<WSResponse, Result>() {
		            public Result apply(WSResponse response) {
		                JsonNode json = response.asJson();
		                System.out.println(json.asText());
		                Logger.debug(json.asText());
		                if(json != null){
		                System.out.println("not null");
		                }
		                JsonNode distance = json.findPath("elements");
		                String dis = distance.findPath("distance").asText();
		                System.out.println(dis);
		                return TODO;
		            }
		        }
		);
		return TODO;
	}
}
