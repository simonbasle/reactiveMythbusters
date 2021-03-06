package reactive.mythbusters.exp9;

import reactive.mythbusters.support.EpisodeService;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Simon Baslé
 */
public class Experiment9Concurrent {

	public static void main(String[] args) {
		EpisodeService episodeService = new EpisodeService();

		Flux.fromIterable(episodeService.topEpisodes())
		    .filter(ep -> ep.getNumber() != 8)
		//TODO How do I make this code run in parallel?
		    .parallel(3)
		    .runOn(Schedulers.newParallel("DescriptionPool"))
		    .log("episode description fetch")
		    .flatMap(episodeService::getDescription)
		//TODO How do I ensure I show the data in a specific thread (ie the UI thread)?
		    .sequential()
		    .publishOn(Schedulers.newSingle("UI Thread"))
		    .log("description notification")
		    .blockLast();
	}

}
