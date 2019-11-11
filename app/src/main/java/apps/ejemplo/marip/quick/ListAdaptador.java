package apps.ejemplo.marip.quick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import apps.ejemplo.marip.quick.R;

import java.util.ArrayList;

public class ListAdaptador extends BaseAdapter {

    private Context contexto;
    private ArrayList<ElementosList> listItems;

    public ListAdaptador(Context contexto, ArrayList<ElementosList> listItems) {
        this.contexto = contexto;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ElementosList Item = (ElementosList) getItem(position);
        convertView = LayoutInflater.from(contexto).inflate(R.layout.elemento_lista, null);
        ImageView Icono= convertView.findViewById(R.id.icono);
        TextView  Elemento = convertView.findViewById(R.id.Elemento);

        Icono.setImageResource(Item.getIcono());
        Elemento.setText(Item.getAccion() );

        return convertView;
    }
}
