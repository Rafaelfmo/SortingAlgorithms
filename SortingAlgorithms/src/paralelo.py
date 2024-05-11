import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
from plotly.graph_objs.layout import XAxis

pd.options.plotting.backend = "plotly"

data_bubble_paralelo = pd.read_csv("bubble_sort_paralelo.csv")
data_merge_paralelo = pd.read_csv("merge_sort_paralelo.csv")
data_insertion_paralelo = pd.read_csv("parallel_insertion_sort.csv")
data_quick_paralelo = pd.read_csv("quick_sort_paralelo.csv")

data_bubble_mean = data_bubble_paralelo.groupby(['Tamanho', 'Threads'])['Tempo'].mean().reset_index()
data_merge_mean = data_merge_paralelo.groupby(['Tamanho', 'Threads'])['Tempo'].mean().reset_index()
data_insertion_mean = data_insertion_paralelo.groupby(['Tamanho', 'Threads'])['Tempo'].mean().reset_index()
data_quick_mean = data_quick_paralelo.groupby(['Tamanho', 'Threads'])['Tempo'].mean().reset_index()

fig = go.Figure()

fig.add_trace(go.Histogram(x=data_bubble_mean['Tamanho'], y=data_bubble_mean['Tempo'], name='Bubble Sort', histfunc='avg'))
fig.add_trace(go.Histogram(x=data_merge_mean['Tamanho'], y=data_merge_mean['Tempo'], name='Merge Sort', histfunc='avg'))
fig.add_trace(go.Histogram(x=data_insertion_mean['Tamanho'], y=data_insertion_mean['Tempo'], name='Insertion Sort', histfunc='avg'))
fig.add_trace(go.Histogram(x=data_quick_mean['Tamanho'], y=data_quick_mean['Tempo'], name='Quick Sort', histfunc='avg'))

datas = [data_bubble_mean,data_merge_mean,data_insertion_mean,data_quick_mean]
nome = ['Bubble Sort', 'Merge Sort', 'Insertion Sort', 'Quick Sort']
for threads in data_bubble_mean['Threads'].unique():
    for i, dataframe in  enumerate(datas):
        fig.add_trace(go.Histogram(x=dataframe[dataframe['Threads'] == threads]['Tamanho'], y = dataframe[dataframe['Threads'] == threads]['Tempo'],\
            name=f'{nome[i]} Com {threads}', histfunc='avg'))


fig.update_layout(xaxis2 = XAxis( 
                                    overlaying='x',
                                    side='top',
                                ),
                  xaxis_title='Tamanho', 
                  yaxis_title='Tempo', 
                  title='Tempo de execução dos algoritmos de ordenação paralela', 
                  template='plotly_dark',
                  legend_title='Algoritmos de ordenação'
                  )

fig.show()