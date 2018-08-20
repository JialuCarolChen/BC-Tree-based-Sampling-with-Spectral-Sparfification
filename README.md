This project aims to investigate the use of biconnected component decomposition in the Spectral Sparsification (SS) approach. 
The Spectral Sparsification approach draws a good proxy graph of the large graph based on the effective resistance of the edges. 
Although it is an effective approach, the runtime of calculating the effective resistance can be too long for practical use. 
Thus, a new approach called Biconnected Components-Based Spectral Sparsification (BCT-SS) is introduced. 
BCT-SS applies the spectral sparsification approach separately on each decomposed biconnected component of the large graph, and then use the effective resistance values calculated from the biconnected components as an approximation of the effective resistance values of the whole graph.
The driving hypothesis of this experiment is that BCT-SS can improve the runtime of Spectral Sparsification by computing the effective resistance values on the decomposed biconnected components instead of the whole graph. 
Another hypothesis is that the resistance values returned from BCT-SS is a good approximation of the effective resistance values of the large graph.
