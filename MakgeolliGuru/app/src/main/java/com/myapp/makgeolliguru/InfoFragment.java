package com.myapp.makgeolliguru;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.myapp.makgeolliguru.profile.ProfileFragment;

public class InfoFragment extends Fragment {

    public InfoFragment(){
        // require a empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView myTextView = view.findViewById(R.id.infoText);

        String info;
        if (getResources().getConfiguration().locale.getLanguage().equals("ko")) {
            info = "<h2>막걸리를 사랑하는 사람들을 위한 첫 번째 앱을 만나보세요!</h2><br>"
                    + "<p>이 전통적인 한국 술은 더 많은 사람들에게 알려지고 사랑받을 가치가 있습니다.</p>"
                    + "<p>우리 앱을 통해 막걸리에 관심 있는 분들과 애호가들을 한자리에 모으고자 합니다:</p><br>"
                    + "<ul>"
                    + "<li>시장에 나와 있는 다양한 막걸리를 탐색하고, 그 맛과 유래에 대해 더 깊이 알아보세요.</li><br>"
                    + "<li>막걸리에 담긴 풍부한 전통을 이야기와 경험을 통해 공유하고 보존하세요.</li><br>"
                    + "</ul>"
                    + "<p>그리고, 이것은 시작에 불과합니다!</p>"
                    + "<p>여러분의 막걸리 여정을 더욱 즐겁게 해줄 새로운 기능들을 개발 중입니다.</p><br>"
                    + "<li>함께 소통하고 배우며 성장할 수 있는 열정적인 커뮤니티를 만들어 갑니다.</li><br>"
                    + "<li>맛있는 막걸리를 즐길 수 있는 최고의 장소를 함께 공유하세요.</li><br>"
                    + "<p>여러분의 피드백은 소중합니다! 제안이나 의견이 있거나, 이 흥미로운 프로젝트에 대해 이야기 나누고 싶다면 "
                    + "makgeolliguru@gmail.com</a>으로 언제든지 연락 주세요.</p><br>"
                    + "<p>이 아름다운 전통을 함께 나누길 기대합니다!</p>";

        }
        else{
            info = "<h2>Discover the First App Dedicated to Makgeolli Lovers!</h2><br>" +
                    "<p>This traditional Korean drink deserves to be better known and appreciated. </p>" +
                    "<p> With our app, we aim to bring together enthusiasts and curious minds to:</p><br>" +
                    "<ul><li>Explore the different Makgeolli available on the market and learn more about their flavors and origins.</li><br>" +
                    "<li>Share and preserve the rich tradition behind Makgeolli through discussions and experiences.</li><br></ul>" +
                    "<p>And this is just the beginning!</p>" +
                    "<p>We’re already working on new features to take your Makgeolli journey even further.</p><br>" +
                    "<li>Build a passionate community where we can exchange, compare, and continuously learn.</li><br>" +
                    "<li>Share the best places to enjoy the delicious Makgeolli.</li><br>" +
                    "<p>Your feedback is invaluable! If you have suggestions, comments, or simply want to chat about this exciting project, feel free to reach out at makgeolliguru@gmail.com</a>.</p><br>" +
                    "<p>Looking forward to sharing this beautiful tradition with you!</p>";
        }


        myTextView.setText(Html.fromHtml(info, Html.FROM_HTML_MODE_COMPACT));


        ImageButton closebtn = view.findViewById(R.id.closebtn);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.flFragment, new ProfileFragment())
                        .addToBackStack(null)  // Optional: Add to back stack for fragment navigation
                        .commit();
            }
        });

        return view;
    }
}
