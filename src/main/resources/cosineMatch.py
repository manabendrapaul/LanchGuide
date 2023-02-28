#! /usr/bin/env python3

import numpy as np
import openai
import pandas as pd
import sys
from openai.embeddings_utils import cosine_similarity
from openai.embeddings_utils import get_embedding

openai.api_key = sys.argv[1]
earnings_search_vector1 = get_embedding(sys.argv[2], engine="text-embedding-ada-002")

earnings_df = pd.read_csv(sys.argv[3], index_col=[0])
df = earnings_df['Embedding'].apply(eval).apply(np.array)
earnings_df["Similarities"] = df.apply(lambda x: cosine_similarity(x, earnings_search_vector1))
earnings_df.sort_values("Similarities", ascending=False)
earnings_df.to_csv(sys.argv[3], mode='w+')
