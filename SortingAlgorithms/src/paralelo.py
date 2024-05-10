import pandas as pd
import plotly.express as px

pd.options.plotting.backend = "plotly"

data_bubble_paralelo = pd.read_csv("C:\\Users\\Rafael Oliveira\\Desktop\\AV2\\bubble_sort_paralelo.csv")
data_merge_paralelo = pd.read_csv("C:\\Users\\Rafael Oliveira\\Desktop\\AV2\\merge_sort_paralelo.csv")
data_insertion_paralelo = pd.read_csv("C:\\Users\\Rafael Oliveira\\Desktop\\AV2\\parallel_insertion_sort.csv")
data_quick_paralelo = pd.read_csv("C:\\Users\\Rafael Oliveira\\Desktop\\AV2\\quick_sort_paralelo.csv")

data_bubble_mean = data_bubble_paralelo.groupby(['Tamanho', 'Threads'])['Tempo'].mean().reset_index()
data_merge_mean = data_merge_paralelo.groupby(['Tamanho', 'Threads'])['Tempo'].mean().reset_index()
data_insertion_mean = data_insertion_paralelo.groupby(['Tamanho', 'Threads'])['Tempo'].mean().reset_index()
data_quick_mean = data_quick_paralelo.groupby(['Tamanho', 'Threads'])['Tempo'].mean().reset_index()


fig = px.histogram({"Merge Sort": data_merge_mean["Tempo"],
                    "Bubble Sort": data_bubble_mean["Tempo"],
                    "Insertion Sort": data_insertion_mean["Tempo"],
                    "Quick Sort": data_quick_mean["Tempo"]},
                    x=data_merge_mean["Tamanho"],
                    y=["Merge Sort", "Bubble Sort", "Insertion Sort", "Quick Sort"],
                    color=data_merge_mean["Threads"],
                    title="Tempo de execução dos algoritmos de ordenação paralela",
                    labels={"value": "Tempo", "index": "Tamanho do vetor"},
                    template="plotly_dark",
                    color_discrete_map={1: "red", 2: "blue", 4: "green", 8: "yellow"}
                   )
fig.show()

