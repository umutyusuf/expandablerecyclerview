# expandablerecyclerview
Alternative Expandable View Utilizing Recycler View

An adapter to allow RecyclerView items to expand/collapse properly. 
Define your parent and corresponding childs and have expand/collapse functionality out of the box.

## Dependency

Add this to the `allProjects` section in your project-level **build.gradle** file:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }

    }
}
```

Add this to module level **build.gradle** file
```gradle
dependencies {
	        implementation 'com.github.umutyusuf:expandablerecyclerview:2.0.0'
}
```

## Basic usage

##### 1)Create index provider
Create index provider to indicate positions for parents and childs

```kotlin
class SampleDataIndexProvider(
    private val mainStreamGenres: List<MainStreamGenre>,
    private val subGenres: Map<MainStreamGenre, List<SubGenre>>
) : ExpandableDataIndexProvider {

    override fun getChildrenSize(parentIndex: Int): Int {
        return subGenres[mainStreamGenres[parentIndex]]?.size ?: 0
    }

    override fun getParentSize(): Int {
        return mainStreamGenres.size
    }
}
```

##### 2) Create adapter

```kotlin
class MusicExpandableViewAdapter(
    private val mainStreamGenres: List<MainStreamGenre>,
    private val subGenres: Map<MainStreamGenre, List<SubGenre>>
) : ExpandableViewAdapter(SampleDataIndexProvider(mainStreamGenres, subGenres)) {

    override fun createParentViewHolder(container: ViewGroup, viewType: Int): MainStreamViewHolder {
        val view = LayoutInflater.from(container.context).inflate(
            R.layout.item_main_stream,
            container, false
        )
        return MainStreamViewHolder(view)
    }

    override fun createChildViewHolder(container: ViewGroup, viewType: Int): SubGenreViewHolder {
        val view = LayoutInflater.from(container.context).inflate(
            R.layout.item_sub_genre,
            container, false
        )
        return SubGenreViewHolder(view)
    }

    inner class MainStreamViewHolder(itemView: View) : ParentViewHolder(itemView) {

        private val nameTextView: TextView =
            itemView.findViewById(R.id.text_view_main_stream_genre_name)
        private val expandIndicatorImageView: AppCompatImageView =
            itemView.findViewById(R.id.image_view_expand_indicator)

        override fun bind(coordinate: Int, state: Int) {
            itemView.setOnClickListener { toggle(coordinate) }
            nameTextView.text = mainStreamGenres[coordinate].name
            setState(state == State.EXPANDED)
        }

        private fun setState(expand: Boolean) {
            if (!expand) {
                expandIndicatorImageView.setImageResource(R.drawable.ic_arrow_down)
            } else {
                expandIndicatorImageView.setImageResource(R.drawable.ic_arrow_up)
            }
        }

    }

    inner class SubGenreViewHolder internal constructor(itemView: View) :
        ChildViewHolder(itemView) {


        private val nameTextView: TextView = itemView.findViewById(R.id.text_view_sub_genre_name)

        override fun bind(coordinate: ChildCoordinate) {
            nameTextView.text = subGenres[mainStreamGenres[coordinate.parentIndex]]
                ?.get(coordinate.childRelativeIndex)
                ?.name ?: ""
        }

    }
}
```
##### 3) Setup your RecyclerView
Finally set adapter to your RecyclerView
```kotlin
val view = findViewById<RecyclerView>(R.id.recycler_view_music_genres)
        val adapter = MusicExpandableViewAdapter(
            TestData.mainStreamGenres,
            TestData.subGenres
        )
        view.adapter = adapter
```

For more advanced usage, see demo application.

 Copyright 2017 Umut Yusuf Tontus

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
