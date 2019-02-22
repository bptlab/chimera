package de.hpi.bpt.chimera.compliance;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.hpi.bpt.chimera.model.petrinet.AbstractPetriNetNode;
import de.hpi.bpt.chimera.model.petrinet.AbstractTranslation;
import de.hpi.bpt.chimera.model.petrinet.PetriNet;
import de.hpi.bpt.chimera.model.petrinet.Place;
import de.hpi.bpt.chimera.model.petrinet.Transition;

public class ComplianceChecker {

	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String LOLA_URL_BASE = "http://lola-webservice/lola.php";

	public String queryLola(String lolaFile, String query) throws Exception {

		String url = LOLA_URL_BASE;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);

		String urlParameters = "custom_formula=on&custom_formula_content=" + query + "&input=" + lolaFile;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		// int responseCode = con.getResponseCode();
		// System.out.println("\nSending 'POST' request to URL : " + url);
		// System.out.println("Post parameters : " + urlParameters);
		// System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\n");
		}
		in.close();

		return response.toString();
	}

	public String extractWitnessPath(String lolaResponse, PetriNet petriNet) {
		Pattern witnessPathPattern = Pattern.compile("custom_check_witness_path = '([^']+)';");
		Pattern stepPattern = Pattern.compile("([pt]_[0-9]+)");
		return extractWitnessElements(lolaResponse, witnessPathPattern, stepPattern, petriNet);
	}

	public String extractWitnessState(String lolaResponse, PetriNet petriNet) {
		Pattern witnessStatePattern = Pattern.compile("custom_check_witness_state = '([^']+)';");
		Pattern stepPattern = Pattern.compile("([pt]_[0-9]+)");
		return extractWitnessElements(lolaResponse, witnessStatePattern, stepPattern, petriNet);
	}

	public String extractWitnessElements(String lolaResponse, Pattern enclosingPattern, Pattern elementPattern,
			PetriNet petriNet) {

		String witnessElementsOutput = "";

		Matcher enclosingMatcher = enclosingPattern.matcher(lolaResponse);
		if (!enclosingMatcher.find()) {
			return "";
		}

		assert (enclosingMatcher.groupCount() == 1);
		final String matchedWitnessGroupContent = enclosingMatcher.group(1);

		Matcher stepMatcher = elementPattern.matcher(matchedWitnessGroupContent);
		while (stepMatcher.find()) {
			assert (stepMatcher.groupCount() > 0);
			final String step = stepMatcher.group(1);
			witnessElementsOutput += getWitnessElementName(step, petriNet);
		}
		return witnessElementsOutput;
	}

	String getWitnessElementName(String witnessStep, PetriNet petriNet) {
		AbstractPetriNetNode petriNetNode;
		if (witnessStep.startsWith("t_")) {
			Optional<Transition> stepTransition = petriNet.getTransitions().stream()
					.filter(p -> p.getPrefixedIdString().equals(witnessStep)).findFirst();
			assert (stepTransition.isPresent());
			petriNetNode = stepTransition.get();
		} else if (witnessStep.startsWith("p_")) {
			Optional<Place> stepPlace = petriNet.getPlaces().stream()
					.filter(p -> p.getPrefixedIdString().equals(witnessStep)).findFirst();
			assert (stepPlace.isPresent());
			petriNetNode = stepPlace.get();
		} else {
			throw new RuntimeException("Witness element must start with 't_' or 'p_', but is '" + witnessStep + "'");
		}

		return petriNetNode.getContext().getPrefixes().stream().collect(Collectors.joining("/")) + "/"
				+ petriNetNode.getName() + "\n";
	}

	public String replaceQueryIdentifiers(PetriNet petriNet, String query) {

		Pattern termPattern = Pattern.compile("\\{([^}]+)\\}");
		Pattern dataObjectStatePattern = Pattern.compile("([a-zA-Z0-9 ]+)\\[([a-zA-Z0-9 ]+)\\]");

		boolean queryChanged;
		do {
			queryChanged = false;
			System.out.println("query: '" + query + "'");

			Matcher termMatcher = termPattern.matcher(query);
			if (termMatcher.find()) {
				final String matchedTermGroup = termMatcher.group(1); // 0 is complete string
				System.out.println("Matched term '" + matchedTermGroup + "'");

				Matcher dataObjectStateMatcher = dataObjectStatePattern.matcher(matchedTermGroup);
				if (dataObjectStateMatcher.find()) {
					assert (dataObjectStateMatcher.groupCount() == 2);
					String dataObject = dataObjectStateMatcher.group(1);
					String dataObjectState = dataObjectStateMatcher.group(2);
					System.out.println("Matched data object '" + dataObject + "' in state '" + dataObjectState + "'");

					Collection<Place> matchingPlaces = petriNet.getPlaces().stream()
							.filter(p -> p.getName().equals(matchedTermGroup)).collect(Collectors.toList());
					if (!matchingPlaces.isEmpty()) {
						if (matchingPlaces.size() != 1) {
							throw new RuntimeException(
									"Found more than one matching place with name " + matchedTermGroup);
						}
						Place referredPlace = matchingPlaces.iterator().next();
						query = termMatcher.replaceFirst(referredPlace.getPrefixedIdString());
						queryChanged = true;
					} else {
						System.out.println("Cannot find place for " + matchedTermGroup);
						throw new RuntimeException("Cannot find place for " + matchedTermGroup);
					}
				} else {
					System.out.println("could not find any data objects");

					final String sanitizedTermGroup = AbstractTranslation.sanitizeName(matchedTermGroup);
					System.out.println("sanitized name is '" + sanitizedTermGroup + "'");

					// TODO match to activity (or something else?)
					Collection<Transition> matchingTransitions = petriNet.getTransitions().stream()
							.filter(t -> t.getName().equals(sanitizedTermGroup)).collect(Collectors.toList());
					if (!matchingTransitions.isEmpty()) {
						assert (matchingTransitions.size() == 1);
						Transition referredTransition = matchingTransitions.iterator().next();
						assert (referredTransition.getOutputPlaces().size() == 1);
						Place outputPlace = referredTransition.getOutputPlaces().iterator().next();
						query = termMatcher.replaceFirst(outputPlace.getPrefixedIdString());
						queryChanged = true;
					} else {
						System.out.println("Cannot find transition for " + sanitizedTermGroup);
						throw new RuntimeException("Cannot find transition for " + sanitizedTermGroup);
					}
				}
			} else {
				System.out.println("could not find any terms");
			}
		} while (queryChanged);

		return query;
	}

}
