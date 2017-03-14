import java.util.Random;

public class AG {
	int T = 10; // numero de geracoes populacionais do criterio de parada
	final static int TAXA_MUTACAO = 5;
	
	int geracao = 0;
	
	float carga = 2.0f;
	
	int index_aux = 0;
	
	final static int TAM_POP = 8;
	final static int TAM_GENE = 6;
	
	int[][] POP = new int[TAM_POP][TAM_GENE];
	int[][] POP_AUX = new int[TAM_POP][TAM_GENE];
	
	public float[] fitness = new float[TAM_POP];
	
	public float[] livros = new float[TAM_GENE];
	
	
	public AG()
	{	
		int i;
		
		for(i = 0; i < TAM_POP; i++)
			fitness[i] = 0.0f;
			
		this.livros[0] = 0.9f;
		this.livros[1] = 1.1f;
		this.livros[2] = 0.7f;
		this.livros[3] = 0.2f;
		this.livros[4] = 0.8f;
		this.livros[5] = 0.6f;
	}
	
	void populacao_inicial()
	{
		int i, g;
		for(i = 0; i < TAM_POP; i++)
			for(g = 0; g < TAM_GENE; g++)
				POP[i][g] = new Random().nextInt(2);
	}
	
	int selecao()
	{
		int i = new Random().nextInt(TAM_POP);
		return i;
	}
	
	void avaliacao()
	{
		int i, g;
		for(i = 0; i < TAM_POP; i++)
		{
			this.fitness[i] = 0.0f;
			for(g = 0; g < TAM_GENE; g++)
				this.fitness[i] += (POP[i][g] * this.livros[g]);
			if(fitness[i] > this.carga)
				fitness[i] = 0.0f;
		}
	}

	void substituicao()
	{
		int i, g;
		for(i = 0; i < TAM_POP; i++)
			for(g = 0; g < TAM_GENE; g++)
				POP[i][g] = POP_AUX[i][g];
	}
	
	void cruzamento_simples()
	{
		int g;
		int pai1 = this.selecao();
		int pai2 = this.selecao();
		
		while(pai1 == pai2)
			pai2 = this.selecao();
		
		for(g = 0; g < TAM_GENE; g++)
		{
			if(g < (TAM_GENE/2))
			{
				POP_AUX[index_aux][g] 		= POP[pai1][g]; 
				POP_AUX[index_aux+1][g] 	= POP[pai2][g];
			} else 
			{
				POP_AUX[index_aux][g]		= POP[pai2][g]; 
				POP_AUX[index_aux+1][g] 	= POP[pai1][g];
			}
		}
	}
	
	void cruzamento_uniforme()
	{
		int g;
		int m;
		int pai1 = this.selecao();
		int pai2 = this.selecao();
		
		while(pai1 == pai2)
			pai2 = this.selecao();
		
		for(g = 0; g < TAM_GENE; g++)
		{
			if(new Random().nextInt(2) == 0)
			{
				POP_AUX[index_aux][g] 		= POP[pai1][g]; 
				POP_AUX[index_aux+1][g] 	= POP[pai2][g];
			} else 
			{
				POP_AUX[index_aux][g]		= POP[pai2][g]; 
				POP_AUX[index_aux+1][g] 	= POP[pai1][g];
			}
		}
	}

	void mutacao()
	{
		int quem = this.selecao();
		int gene = new Random().nextInt(TAM_GENE);
		POP_AUX[quem][gene] = POP_AUX[quem][gene] == 0 ? 1 : 0;
	}
	
	void mostra_pop_melhor()
	{
		int i;
		int iMelhor = 0;
		float vMelhor = this.fitness[0];
		
		for(i = 1; i < TAM_POP; i++)
		{
			if(vMelhor < this.fitness[i])
			{
				vMelhor = this.fitness[i];
				iMelhor = i;
			}
		}
		
		System.out.print("G: " + this.geracao + " Ind: " + iMelhor + " [");
		for(i = 0; i < TAM_GENE; i++)
		{
			System.out.print("  " + POP[iMelhor][i]);
		}
		System.out.println(" ] = " + fitness[iMelhor]);

	}
	
	void mostra_pop()
	{
		int i, g;
		for(i = 0; i < TAM_POP; i++)
		{
			System.out.print("Ind: " + i + " [");
			for(g = 0; g < TAM_GENE; g++)
			{
				System.out.print("  " + POP[i][g]);
			}
			System.out.println(" ] = " + fitness[i]);
		}
	}
	public static void main(String[] args) {
		//int pai1, pai2;
		int T = 10;
		int m = 0; // loop mutacao
		AG ag = new AG();
		ag.populacao_inicial();
		
		int qtd_mutacao = (TAM_POP * TAXA_MUTACAO) / 100;
		
		while(ag.geracao < T)
		{
			ag.avaliacao();
			ag.mostra_pop_melhor();
			
			// gera descendentes a partir de cruzamento
			while(ag.index_aux < ag.TAM_POP)
			{
				ag.cruzamento_simples();
				ag.index_aux += 2;
			}
			
			// realiza mutacoes na populacao auxiliar.
			m = 0;
			while(m < qtd_mutacao)
			{
				ag.mutacao();
				m++;
			}
			
			ag.index_aux = 0;
			
			ag.substituicao();
			ag.geracao++;
		}
	}
}
