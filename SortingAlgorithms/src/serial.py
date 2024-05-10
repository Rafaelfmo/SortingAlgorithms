import pandas as pd
import plotly.express as px

pd.options.plotting.backend = "plotly"

data_bubble_serial = pd.read_csv("C:\\Users\\Rafael Oliveira\\Desktop\\AV2\\bubble_sort_serial.csv")
data_merge_serial = pd.read_csv("C:\\Users\\Rafael Oliveira\\Desktop\\AV2\\merge_sort_serial.csv")
data_insertion_serial = pd.read_csv("C:\\Users\\Rafael Oliveira\\Desktop\\AV2\\serial_insertion_sort.csv")
data_quick_serial = pd.read_csv("C:\\Users\\Rafael Oliveira\\Desktop\\AV2\\quick_sort_serial.csv")

fig = px.line({"Merge Sort": data_merge_serial["Tempo"], 
               "Bubble Sort": data_bubble_serial["Tempo"], 
               "Insertion Sort": data_insertion_serial["Tempo"], 
               "Quick Sort": data_quick_serial["Tempo"]},
                x=data_merge_serial["Tamanho"],
                y=["Merge Sort", "Bubble Sort", "Insertion Sort", "Quick Sort"],
                title="Tempo de execução dos algoritmos de ordenação serial", 
                labels={"value": "Tempo", "index": "Tamanho do vetor"},
                template="plotly_dark"
               )

fig.show()
