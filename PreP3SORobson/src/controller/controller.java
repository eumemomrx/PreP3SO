package controller;

import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;

public class controller {
	public controller() {
		super();
	}

	public String os() {
		String os = System.getProperty("os.name");
		return os;
	}

	@SuppressWarnings({ "unchecked" })
	public void lg() throws IOException {
		String caminho = null;
		if (os().contains("Windows")) {
			caminho = "C:\\TEMP";
		} else {
			caminho = "\tmp";
		}
		File dir = new File(caminho);
		if (!dir.exists()) {
			dir.mkdir();
		}
		String nomeArq = "wiki.json";
		File arq = new File(dir, nomeArq);
		if (!arq.exists()) {
			try {
				arq.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			String url = "https://wikimedia.org/api/rest_v1/metrics/pageviews/per-article/en.wikipedia/all-access/all-agents/Tiger_King/daily/20210901/20210930";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String linha = br.readLine();
				String linhaAux[] = linha.split("},");
				FileWriter fw = new FileWriter(arq);
				JSONObject jsonObject = new JSONObject();
				for (int i = 0; i < linhaAux.length; i++) {
					jsonObject.put(' ', linhaAux[i]);
					try {
						fw.append(jsonObject.toJSONString());
					} catch (IOException e) {
						System.out.println("Deu pau");
					}
				}
				fw.close();
			} else {
				System.out.println("Erro " + responseCode);
			}
		} catch (IOException e) {
			System.out.println("Erro de leitura");
		}
	}

	public void leitura() throws IOException {
		String path = "C:\\TEMP";
		String file = "wiki.json";
		File wiki = new File(path, file);
		FileInputStream fluxo = new FileInputStream(wiki);
		InputStreamReader leitor = new InputStreamReader(fluxo);
		BufferedReader buffer = new BufferedReader(leitor);
		String linha = buffer.readLine();
		String[] linhaDia = linha.split("}");
		for (int i = 0; i < linhaDia.length; i++) {
			String[] dados = linhaDia[i].split(",");
			for (int j = 0; j < dados.length; j++) {
				if (dados[j].contains("timestamp")) {
					String data = dados[j].substring(16, 24);
					String views = dados[j + 3].substring(10);
					if (i < 29) {
						views = views.substring(0, views.length() - 1);
					}
					System.out.println("Data: " + data + " Views: " + views);
				}
			}

		}
	}
}
